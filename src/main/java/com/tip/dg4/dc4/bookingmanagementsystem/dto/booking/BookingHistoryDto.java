package com.tip.dg4.dc4.bookingmanagementsystem.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookingHistoryDto {
    private UUID id;

    private String hotelName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstant.DATE_FORMAT)
    private LocalDate checkInDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstant.DATE_FORMAT)
    private LocalDate checkOutDate;

    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstant.DATE_FORMAT)
    private LocalDate bookingDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstant.DATE_FORMAT)
    private LocalDate paymentDate;

    private int personQuantity;

    private BigDecimal totalPrice;

    private String imageURL;

    private List<BookingDetailDto> rooms;
}
