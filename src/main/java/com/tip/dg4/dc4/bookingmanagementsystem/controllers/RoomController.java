package com.tip.dg4.dc4.bookingmanagementsystem.controllers;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.room.RoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.room.SearchRoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.services.RoomService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.res.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/{hotelId}")
    public ResponseEntity<DataResponse> createRooms(@PathVariable UUID hotelId, @RequestBody List<RoomDto> roomDTOs) {
        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse dataResponse = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Room(s) was created successfully",
                roomService.createRooms(hotelId, roomDTOs)
        );

        return new ResponseEntity<>(dataResponse, httpStatus);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<DataResponse> getRoomsByHotelId(@PathVariable UUID hotelId) {
        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse dataResponse = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Room(s) was got successfully",
                roomService.getRoomsByHotelId(hotelId)
        );

        return new ResponseEntity<>(dataResponse, httpStatus);
    }

    @PostMapping("/getAvailableRooms/{hotelId}")
    public ResponseEntity<DataResponse> getAvailableRoomsByHotelId(@PathVariable UUID hotelId,
                                                                   @RequestBody(required = false) SearchRoomDto searchRoomDto) {
        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse dataResponse = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Room(s) was got successfully",
                roomService.getAvailableRoomsByHotelId(hotelId, searchRoomDto)
        );

        return new ResponseEntity<>(dataResponse, httpStatus);
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<DataResponse> updateRoomByHotelId(@PathVariable UUID hotelId, @RequestBody RoomDto roomDto) {
        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse dataResponse = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Room was updated successfully",
                roomService.updateRoomById(hotelId, roomDto)
        );

        return new ResponseEntity<>(dataResponse, httpStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse> deleteRoomById(@PathVariable UUID id) {
        roomService.deleteRoomById(id);

        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse dataResponse = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Room was deleted successfully"
        );

        return new ResponseEntity<>(dataResponse, httpStatus);
    }
}
