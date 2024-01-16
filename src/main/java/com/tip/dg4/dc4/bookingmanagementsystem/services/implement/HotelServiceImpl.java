package com.tip.dg4.dc4.bookingmanagementsystem.services.implement;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.GetHotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.HotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.SearchHotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.BadRequestException;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.NotFoundException;
import com.tip.dg4.dc4.bookingmanagementsystem.mappers.HotelMapper;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Hotel;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Room;
import com.tip.dg4.dc4.bookingmanagementsystem.repositories.HotelRepository;
import com.tip.dg4.dc4.bookingmanagementsystem.services.BookingHistoryService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.HotelService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.ImageService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.RoomService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.ExceptionConstant;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final ImageService imageService;
    @Lazy private final BookingHistoryService bookingHistoryService;
    @Lazy private final RoomService roomService;

    @Transactional
    @Override
    public HotelDto createHotel(HotelDto hotelDto) {
        if (invalidHotel(hotelDto)) {
            throw new BadRequestException(ExceptionConstant.COMMON_E001);
        }
        if (hotelRepository.existsByNameAndAddress(hotelDto.getName(), hotelDto.getAddress())) {
            throw new BadRequestException(ExceptionConstant.HOTEL_E001);
        }

        Hotel hotel = hotelMapper.convertDtoToModel(hotelDto);
        hotelRepository.save(hotel);
        List<String> imageURLs = imageService.createImages(hotelDto.getImageURLs(), hotel.getId());

        return hotelMapper.convertModelToDTO(hotel, imageURLs);
    }

    @Override
    public List<HotelDto> getHotels() {
        List<HotelDto> hotels = new ArrayList<>();

        for (Hotel hotel : hotelRepository.findAll()) {
            List<String> imageURLs = imageService.getImagesByObjectId(hotel.getId());
            hotels.add(hotelMapper.convertModelToDTO(hotel, imageURLs));
        }

        return hotels;
    }

    @Override
    public List<GetHotelDto> getHotelsHasAvailableRooms(SearchHotelDto searchHotelDto) {
        return ObjectUtils.isEmpty(searchHotelDto) ?
                getHotelsHasAvailableRooms() :
                searchHotelsHasAvailableRooms(searchHotelDto);
    }

    private List<GetHotelDto> getHotelsHasAvailableRooms() {
        List<Hotel> hotels = hotelRepository.findAll();
        if (CollectionUtils.isEmpty(hotels)) return Collections.emptyList();

        List<GetHotelDto> getHotelDTOs = new ArrayList<>();
        for (Hotel hotel : hotels) {
            LocalDate currentDate = LocalDate.now();
            List<Room> availableRooms = roomService.getAvailableRooms(hotel, currentDate, currentDate);
            if (CollectionUtils.isEmpty(availableRooms)) continue;

            String imageURL = imageService.getImagesByObjectId(hotel.getId()).get(0);
            Map<String, Integer> rooms = availableRooms.stream()
                    .collect(Collectors.toMap(
                            room -> room.getType().getCode(), room -> 1, Integer::sum, LinkedHashMap::new)
                    );

            getHotelDTOs.add(hotelMapper.convertModelToGetHotelDTO(hotel, imageURL, rooms));
        }

        return getHotelDTOs;
    }

    private List<GetHotelDto> searchHotelsHasAvailableRooms(SearchHotelDto searchHotelDto) {
        List<Hotel> hotels;
        if (ObjectUtils.isEmpty(searchHotelDto.getDestination())) {
            hotels = hotelRepository.findAll();
        } else {
            hotels = hotelRepository.findAll().stream()
                    .filter(hotel -> searchHotelDto.getDestination().equals(hotel.getDestination().getValue()))
                    .toList();
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate checkInDate = AppUtil.defaultIfNull(searchHotelDto.getCheckInDate(), currentDate);
        LocalDate checkOutDate = AppUtil.defaultIfNull(searchHotelDto.getCheckOutDate(), currentDate);
        if (checkInDate.isBefore(currentDate) || checkInDate.isAfter(checkOutDate)) {
            throw new BadRequestException(ExceptionConstant.ROOM_E005);
        }
        int personQuantity = AppUtil.defaultIfNull(searchHotelDto.getPersonQuantity(), 0);
        List<GetHotelDto> hotelsHasAvailableRooms = new ArrayList<>();
        for (Hotel hotel : hotels) {
            List<Room> availableRooms = roomService.getAvailableRooms(hotel, checkInDate, checkOutDate);
            int totalPersonQuantity = availableRooms.stream().mapToInt(room -> room.getType().getMaxPersonNumber()).sum();

            if (personQuantity <= totalPersonQuantity) {
                String imageURL = imageService.getImagesByObjectId(hotel.getId()).get(0);
                Map<String, Integer> rooms = availableRooms.stream()
                        .collect(Collectors.toMap(
                                room -> room.getType().getCode(), room -> 1, Integer::sum, LinkedHashMap::new)
                        );

                hotelsHasAvailableRooms.add(hotelMapper.convertModelToGetHotelDTO(hotel, imageURL, rooms));
            }
        }

        return hotelsHasAvailableRooms;
    }

    @Override
    public HotelDto getHotelById(UUID id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionConstant.HOTEL_E002 + id));
        List<String> imageURLs = Optional.ofNullable(imageService.getImagesByObjectId(hotel.getId())).orElse(Collections.emptyList());

        return hotelMapper.convertModelToDTO(hotel, imageURLs);
    }

    @Transactional
    @Override
    public HotelDto updateHotel(UUID id, HotelDto hotelDto) {
        if (invalidHotel(hotelDto)) {
            throw new BadRequestException(ExceptionConstant.COMMON_E001);
        }
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionConstant.HOTEL_E002 + id));
        if (!hotelDto.getName().equals(hotel.getName()) &&
                hotelRepository.existsByNameAndAddress(hotelDto.getName(), hotelDto.getAddress())) {
            throw new BadRequestException(ExceptionConstant.HOTEL_E001);
        }

        List<String> imageURLs = imageService.updateImages(hotelDto.getImageURLs(), hotel.getId());
        hotel.setName(hotelDto.getName());
        hotel.setAddress(hotelDto.getAddress());
        hotel.setDescription(hotelDto.getDescription());
        hotel.setActive(hotelDto.isActive());
        hotelRepository.save(hotel);

        return hotelMapper.convertModelToDTO(hotel, imageURLs);
    }

    @Override
    public void deleteHotelById(UUID id) {
        Hotel hotel = findById(id).orElseThrow(() -> new NotFoundException(ExceptionConstant.HOTEL_E002 + id));

        if (bookingHistoryService.existsByHotel(hotel)) {
            throw new BadRequestException(ExceptionConstant.HOTEL_E003);
        }

        imageService.deleteImagesByObjectId(id);
        hotelRepository.deleteById(id);
    }

    @Override
    public Optional<Hotel> findById(UUID id) {
        return hotelRepository.findById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return hotelRepository.existsById(id);
    }

    private boolean invalidHotel(HotelDto hotelDto) {
        return ObjectUtils.isEmpty(hotelDto.getName()) ||
                CollectionUtils.isEmpty(hotelDto.getImageURLs()) ||
                ObjectUtils.isEmpty(hotelDto.getAddress()) ||
                ObjectUtils.isEmpty(hotelDto.getDescription()) ||
                ObjectUtils.isEmpty(hotelDto.getDestination());
    }
}
