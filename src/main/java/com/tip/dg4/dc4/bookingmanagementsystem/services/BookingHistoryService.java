package com.tip.dg4.dc4.bookingmanagementsystem.services;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingHistoryDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingRoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.GetAllBookingHistoryDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.BookingHistory;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Hotel;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.BookingStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingHistoryService {
    void bookingRooms(BookingRoomDto bookingRoomDto);

    List<GetAllBookingHistoryDto> getBookingHistories();

    List<BookingHistoryDto> getByUserId(UUID userId, BookingStatus status);

    void updateStatus(UUID id, BookingStatus status);

    Optional<BookingHistory> findById(UUID id);

    boolean existsByHotel(Hotel hotel);
}
