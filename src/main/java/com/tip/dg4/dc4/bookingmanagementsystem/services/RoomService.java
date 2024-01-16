package com.tip.dg4.dc4.bookingmanagementsystem.services;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.room.GetRoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.room.RoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.room.SearchRoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Hotel;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RoomService {
    List<RoomDto> createRooms(UUID hotelId, List<RoomDto> roomDTOs);

    List<RoomDto> getRoomsByHotelId(UUID hotelId);

    List<GetRoomDto> getAvailableRoomsByHotelId(UUID hotelId, SearchRoomDto searchRoomDto);

    RoomDto updateRoomById(UUID hotelId, RoomDto roomDto);

    void deleteRoomById(UUID id);

    List<Room> getAvailableRooms(Hotel hotel, LocalDate checkInDate, LocalDate checkOutDate);
}
