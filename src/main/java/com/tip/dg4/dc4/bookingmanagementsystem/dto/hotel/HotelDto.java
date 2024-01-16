package com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class HotelDto {
    private UUID id;

    private String name;

    private String address;

    private String description;

    private String destination;

    private List<String> imageURLs;

    @JsonProperty(value = "isActive")
    private boolean isActive;
}
