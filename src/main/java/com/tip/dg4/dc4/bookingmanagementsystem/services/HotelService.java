package com.tip.dg4.dc4.bookingmanagementsystem.services;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.GetHotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.HotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.SearchHotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Hotel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HotelService {
    HotelDto createHotel(HotelDto hotelDto);

    List<HotelDto> getHotels();

    List<GetHotelDto> getHotelsHasAvailableRooms(SearchHotelDto searchHotelDto);

    HotelDto getHotelById(UUID id);

    void deleteHotelById(UUID id);

    HotelDto updateHotel(UUID id, HotelDto hotelDto);

    Optional<Hotel> findById(UUID id);

    boolean existsById(UUID id);
}
