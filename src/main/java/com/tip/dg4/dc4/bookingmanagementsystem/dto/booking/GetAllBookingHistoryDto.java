package com.tip.dg4.dc4.bookingmanagementsystem.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetAllBookingHistoryDto {
    private UUID id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstant.DATE_FORMAT)
    private LocalDate checkInDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstant.DATE_FORMAT)
    private LocalDate checkOutDate;

    private String hotelName;

    private int roomQuantity;

    private long duration;

    private String customer;

    private String email;

    private BigDecimal totalPrice;

    private String status;
}
