package com.tip.dg4.dc4.bookingmanagementsystem.mappers;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.GetHotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.HotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Hotel;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.Destination;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HotelMapper {
    public Hotel convertDtoToModel(HotelDto hotelDto) {
        return Hotel.builder()
                .id(hotelDto.getId())
                .name(hotelDto.getName())
                .address(hotelDto.getAddress())
                .description(hotelDto.getDescription())
                .destination(Destination.getDestination(hotelDto.getDestination()))
                .isActive(hotelDto.isActive())
                .build();
    }

    public HotelDto convertModelToDTO(Hotel hotel, List<String> imageURLs) {
        return HotelDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .address(hotel.getAddress())
                .description(hotel.getDescription())
                .destination(Destination.getValue(hotel.getDestination()))
                .imageURLs(imageURLs)
                .isActive(hotel.isActive())
                .build();
    }

    public GetHotelDto convertModelToGetHotelDTO(Hotel hotel, String imageURL, Map<String, Integer> rooms) {
        return GetHotelDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .address(hotel.getAddress())
                .destination(hotel.getDestination().getValue())
                .description(hotel.getDescription())
                .imageURL(imageURL)
                .rooms(rooms)
                .build();
    }
}
