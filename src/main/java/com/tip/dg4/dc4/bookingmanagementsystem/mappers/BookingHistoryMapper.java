package com.tip.dg4.dc4.bookingmanagementsystem.mappers;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingDetailDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingHistoryDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.GetAllBookingHistoryDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.BookingHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookingHistoryMapper {
    private final BookingDetailMapper bookingDetailMapper;

    public BookingHistoryDto convertEntityToDto(BookingHistory bookingHistory, String imageURL) {
        List<BookingDetailDto> rooms = Optional.ofNullable(bookingHistory.getBookingDetails()).orElse(Collections.emptyList())
                .stream().map(bookingDetailMapper::convertEntityToDto).toList();
        long duration = ChronoUnit.DAYS.between(bookingHistory.getCheckInDate(), bookingHistory.getCheckOutDate().plusDays(1));
        BigDecimal totalPrice = bookingHistory.getTotalPrice().multiply(new BigDecimal(duration));

        return BookingHistoryDto.builder()
                .id(bookingHistory.getId())
                .hotelName(bookingHistory.getHotel().getName())
                .checkInDate(bookingHistory.getCheckInDate())
                .checkOutDate(bookingHistory.getCheckOutDate().plusDays(1))
                .status(bookingHistory.getStatus().getValue())
                .bookingDate(bookingHistory.getBookingDate())
                .paymentDate(bookingHistory.getPaymentDate())
                .personQuantity(bookingHistory.getPersonQuantity())
                .totalPrice(totalPrice)
                .imageURL(imageURL)
                .rooms(rooms).build();
    }

    public GetAllBookingHistoryDto convertEntityToDto(BookingHistory bookingHistory) {
        long duration = ChronoUnit.DAYS.between(bookingHistory.getCheckInDate(), bookingHistory.getCheckOutDate().plusDays(1));
        String customer = bookingHistory.getUser().getSurname() + " " + bookingHistory.getUser().getName();
        BigDecimal totalPrice = bookingHistory.getTotalPrice().multiply(new BigDecimal(duration));

        return GetAllBookingHistoryDto.builder()
                .id(bookingHistory.getId())
                .checkInDate(bookingHistory.getCheckInDate())
                .checkOutDate(bookingHistory.getCheckOutDate().plusDays(1))
                .hotelName(bookingHistory.getHotel().getName())
                .roomQuantity(bookingHistory.getBookingDetails().size())
                .duration(duration)
                .customer(customer)
                .email(bookingHistory.getUser().getEmail())
                .totalPrice(totalPrice)
                .status(bookingHistory.getStatus().getValue()).build();
    }
}
