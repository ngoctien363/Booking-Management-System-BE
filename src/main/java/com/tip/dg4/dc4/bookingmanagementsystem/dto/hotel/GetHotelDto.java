package com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GetHotelDto {
    private UUID id;

    private String name;

    private String address;

    private String destination;

    private String description;

    private String imageURL;

    private Map<String, Integer> rooms;
}
