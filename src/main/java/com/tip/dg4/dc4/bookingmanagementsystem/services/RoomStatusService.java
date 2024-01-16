package com.tip.dg4.dc4.bookingmanagementsystem.services;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingRoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Room;
import com.tip.dg4.dc4.bookingmanagementsystem.models.RoomStatus;

import java.time.LocalDate;
import java.util.UUID;

public interface RoomStatusService {
    boolean existsByRoomId(UUID roomId);

    boolean isAvailableByRoomIdAndDate(UUID roomId, LocalDate checkInDate, LocalDate checkOutDate);

    RoomStatus create(Room room, BookingRoomDto bookingRoomDto);

    void delete(RoomStatus roomStatus);
}
