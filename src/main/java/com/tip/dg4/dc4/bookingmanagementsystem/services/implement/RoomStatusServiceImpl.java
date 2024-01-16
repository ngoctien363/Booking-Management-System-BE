package com.tip.dg4.dc4.bookingmanagementsystem.services.implement;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingRoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Room;
import com.tip.dg4.dc4.bookingmanagementsystem.models.RoomStatus;
import com.tip.dg4.dc4.bookingmanagementsystem.repositories.RoomStatusRepository;
import com.tip.dg4.dc4.bookingmanagementsystem.services.RoomService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.RoomStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class RoomStatusServiceImpl implements RoomStatusService {
    private final RoomStatusRepository roomStatusRepository;
    @Lazy
    private final RoomService roomService;

    @Override
    public boolean existsByRoomId(UUID roomId) {
        return roomStatusRepository.existsByRoomId(roomId);
    }

    @Override
    public boolean isAvailableByRoomIdAndDate(UUID roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        long count = roomStatusRepository.findByRoomId(roomId).stream()
                .filter(roomStatus -> isWithinDate(checkInDate, checkOutDate, roomStatus))
                .count();

        return count == 0;
    }

    @Override
    public RoomStatus create(Room room, BookingRoomDto bookingRoomDto) {
        RoomStatus roomStatus = RoomStatus.builder()
                .checkInDate(bookingRoomDto.getCheckInDate())
                .checkOutDate(bookingRoomDto.getCheckOutDate())
                .room(room)
                .build();

        return roomStatusRepository.save(roomStatus);
    }

    @Override
    public void delete(RoomStatus roomStatus) {
        roomStatusRepository.delete(roomStatus);
    }

    private boolean isWithinDate(LocalDate checkInDate, LocalDate checkOutDate, RoomStatus roomStatus) {
        return (checkInDate.isAfter(roomStatus.getCheckInDate()) || checkInDate.isEqual(roomStatus.getCheckInDate()))
                && (checkInDate.isBefore(roomStatus.getCheckOutDate()) || checkInDate.isEqual(roomStatus.getCheckOutDate()))
                ||
                (checkOutDate.isAfter(roomStatus.getCheckInDate()) || checkOutDate.isEqual(roomStatus.getCheckInDate()))
                        && (checkOutDate.isBefore(roomStatus.getCheckOutDate()) || checkOutDate.isEqual(roomStatus.getCheckOutDate()));
    }
}
