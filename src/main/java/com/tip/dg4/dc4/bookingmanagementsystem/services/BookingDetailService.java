package com.tip.dg4.dc4.bookingmanagementsystem.services;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingDetailDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingRoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.BookingDetail;
import com.tip.dg4.dc4.bookingmanagementsystem.models.BookingHistory;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Hotel;

import java.util.List;
import java.util.UUID;

public interface BookingDetailService {
    void create(BookingHistory bookingHistory, Hotel hotel, BookingRoomDto bookingRoomDto);

    List<BookingDetailDto> getByBookingHistoryId(UUID bookingHistoryId);
}
