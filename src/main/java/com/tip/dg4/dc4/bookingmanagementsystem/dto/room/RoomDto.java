package com.tip.dg4.dc4.bookingmanagementsystem.dto.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoomDto {
    private UUID id;

    private String roomNumber;

    private BigDecimal price;

    private String convenient;

    private String description;

    private Integer maxPersonNumber;

    private String roomType;

    @JsonProperty("isActive")
    private boolean isActive;
}