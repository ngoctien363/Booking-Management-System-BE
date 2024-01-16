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
public class GetRoomDto {
    private UUID id;

    private String roomType;

    private String convenient;

    private int maxPersonNumber;

    private BigDecimal price;

    private int availableRooms;

    @JsonProperty("isActive")
    private boolean isActive;
}
