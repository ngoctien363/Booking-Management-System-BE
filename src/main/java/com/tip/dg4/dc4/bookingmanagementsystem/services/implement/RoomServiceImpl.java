package com.tip.dg4.dc4.bookingmanagementsystem.services.implement;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.room.GetRoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.room.RoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.room.SearchRoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.BadRequestException;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.NotFoundException;
import com.tip.dg4.dc4.bookingmanagementsystem.mappers.RoomMapper;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Hotel;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Room;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.RoomType;
import com.tip.dg4.dc4.bookingmanagementsystem.repositories.RoomRepository;
import com.tip.dg4.dc4.bookingmanagementsystem.services.HotelService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.RoomService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.RoomStatusService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.ExceptionConstant;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelService hotelService;
    private final RoomStatusService roomStatusService;
    private final RoomMapper roomMapper;

    @Transactional
    @Override
    public List<RoomDto> createRooms(UUID hotelId, List<RoomDto> roomDTOs) {
        Hotel hotel = hotelService.findById(hotelId)
                .orElseThrow(() -> new NotFoundException(ExceptionConstant.HOTEL_E002 + hotelId));
        if (CollectionUtils.isEmpty(roomDTOs)) return Collections.emptyList();
        List<RoomDto> rooms = new ArrayList<>();

        roomDTOs.forEach(roomDto -> {
            if (invalidRoom(roomDto)) {
                throw new BadRequestException(ExceptionConstant.COMMON_E001);
            }
            if (roomDto.getRoomNumber().length() < 3 || AppUtil.nonNumeric(roomDto.getRoomNumber())) {
                throw new BadRequestException(ExceptionConstant.ROOM_E006);
            }
            if (roomRepository.existsByRoomNumberAndHotel(roomDto.getRoomNumber(), hotel)) {
                throw new BadRequestException(ExceptionConstant.ROOM_E001);
            }
            Room room = roomMapper.convertDtoToModel(roomDto);
            room.setHotel(hotel);
            roomRepository.save(room);
            rooms.add(roomMapper.convertModelToDTO(room));
        });

        return rooms;
    }

    @Override
    public List<RoomDto> getRoomsByHotelId(UUID hotelId) {
        if (!hotelService.existsById(hotelId)) {
            throw new NotFoundException(ExceptionConstant.HOTEL_E002 + hotelId);
        }
        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        if (CollectionUtils.isEmpty(rooms)) return Collections.emptyList();

        return rooms.stream().map(roomMapper::convertModelToDTO).toList();
    }

    @Override
    public List<GetRoomDto> getAvailableRoomsByHotelId(UUID hotelId, SearchRoomDto searchRoomDto) {
        Hotel hotel = hotelService.findById(hotelId)
                .orElseThrow(() -> new NotFoundException(ExceptionConstant.HOTEL_E002));

        LocalDate currentDate = LocalDate.now();
        LocalDate checkInDate = currentDate;
        LocalDate checkOutDate = currentDate;
        int personQuantity = 0;
        if (searchRoomDto != null) {
            checkInDate = AppUtil.defaultIfNull(searchRoomDto.getCheckInDate(), currentDate);
            checkOutDate = AppUtil.defaultIfNull(searchRoomDto.getCheckOutDate(), currentDate);
            personQuantity = AppUtil.defaultIfNull(searchRoomDto.getPersonQuantity(), 0);
        }
        if (checkInDate.isBefore(currentDate) || checkInDate.isAfter(checkOutDate)) {
            throw new BadRequestException(ExceptionConstant.ROOM_E005);
        }

        List<Room> availableRooms = this.getAvailableRooms(hotel, checkInDate, checkOutDate);
        if (CollectionUtils.isEmpty(availableRooms)) return Collections.emptyList();

        List<GetRoomDto> roomDTOs = new ArrayList<>();
        int maxPersonNumber = 0;
        for (Room availableRoom : availableRooms) {
            Optional<GetRoomDto> optionalRoomDto = roomDTOs.stream()
                    .filter(r -> availableRoom.getType().equals(RoomType.getType(r.getRoomType()))).findFirst();
            GetRoomDto roomDto;
            if (optionalRoomDto.isEmpty()) {
                roomDto = GetRoomDto.builder()
                        .id(availableRoom.getId())
                        .roomType(availableRoom.getType().getValue())
                        .convenient(availableRoom.getConvenient())
                        .maxPersonNumber(availableRoom.getType().getMaxPersonNumber())
                        .price(availableRoom.getType().getPriceOneNight())
                        .availableRooms(1)
                        .isActive(availableRoom.isActive())
                        .build();
                roomDTOs.add(roomDto);
            } else {
                roomDto = optionalRoomDto.get();
                roomDto.setAvailableRooms(roomDto.getAvailableRooms() + 1);
                roomDTOs.set(roomDTOs.indexOf(optionalRoomDto.get()), roomDto);
            }
            maxPersonNumber += roomDto.getMaxPersonNumber();
        }

        return (personQuantity <= maxPersonNumber) ? roomDTOs : Collections.emptyList();
    }

    @Override
    public RoomDto updateRoomById(UUID hotelId, RoomDto roomDto) {
        Hotel hotel = hotelService.findById(hotelId)
                .orElseThrow(() -> new NotFoundException(ExceptionConstant.HOTEL_E002));
        Room room = roomRepository.findById(roomDto.getId())
                .orElseThrow(() -> new NotFoundException(ExceptionConstant.ROOM_E002));
        if (roomDto.getRoomNumber().length() < 3 || AppUtil.nonNumeric(roomDto.getRoomNumber())) {
            throw new BadRequestException(ExceptionConstant.ROOM_E006);
        }
        if (!roomDto.getRoomNumber().equals(room.getRoomNumber()) &&
                roomRepository.existsByRoomNumberAndHotel(roomDto.getRoomNumber(), hotel)) {
            throw new BadRequestException(ExceptionConstant.ROOM_E001);
        }
        RoomType roomType = RoomType.getType(roomDto.getRoomType());
        if (RoomType.UNDEFINED.equals(roomType)) {
            throw new BadRequestException(ExceptionConstant.ROOM_E003);
        }

        room.setRoomNumber(roomDto.getRoomNumber());
        room.setConvenient(roomDto.getConvenient());
        room.setDescription(roomDto.getDescription());
        room.setType(roomType);
        room.setActive(roomDto.isActive());

        return roomMapper.convertModelToDTO(roomRepository.save(room));
    }

    @Override
    public void deleteRoomById(UUID id) {
        if (!roomRepository.existsById(id)) {
            throw new NotFoundException(ExceptionConstant.ROOM_E002);
        }
        if (roomStatusService.existsByRoomId(id)) {
            throw new BadRequestException(ExceptionConstant.ROOM_E004);
        }

        roomRepository.deleteById(id);
    }

    @Override
    public List<Room> getAvailableRooms(Hotel hotel, LocalDate checkInDate, LocalDate checkOutDate) {
        return roomRepository.findAll().stream()
                .filter(room -> hotel.equals(room.getHotel()) &&
                        roomStatusService.isAvailableByRoomIdAndDate(room.getId(), checkInDate, checkOutDate))
                .toList();
    }

    private boolean invalidRoom(RoomDto roomDto) {
        return (ObjectUtils.isEmpty(roomDto.getRoomNumber())) ||
                ObjectUtils.isEmpty(roomDto.getConvenient()) ||
                ObjectUtils.isEmpty(roomDto.getDescription()) ||
                ObjectUtils.isEmpty(roomDto.getRoomType());
    }
}
