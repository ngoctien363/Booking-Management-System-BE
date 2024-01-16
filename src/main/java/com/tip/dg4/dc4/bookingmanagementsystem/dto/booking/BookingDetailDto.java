package com.tip.dg4.dc4.bookingmanagementsystem.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookingDetailDto {
    private UUID id;

    private String roomNumber;

    private String roomType;

    private String convenient;

    private BigDecimal price;

    private int maxPersonNum;
}
