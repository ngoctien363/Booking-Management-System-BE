package com.tip.dg4.dc4.bookingmanagementsystem.mappers;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingDetailDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.BookingDetail;
import org.springframework.stereotype.Component;

@Component
public class BookingDetailMapper {
    public BookingDetailDto convertEntityToDto(BookingDetail bookingDetail) {
        return BookingDetailDto.builder()
                .id(bookingDetail.getId())
                .roomNumber(bookingDetail.getRoomNumber())
                .roomType(bookingDetail.getRoomType().getValue())
                .convenient(bookingDetail.getConvenient())
                .price(bookingDetail.getPrice())
                .maxPersonNum(bookingDetail.getMaxPersonNum())
                .build();
    }
}
