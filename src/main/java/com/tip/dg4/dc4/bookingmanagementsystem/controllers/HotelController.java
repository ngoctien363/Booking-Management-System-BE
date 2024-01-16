package com.tip.dg4.dc4.bookingmanagementsystem.controllers;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.HotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.SearchHotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.services.HotelService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.res.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @PostMapping()
    public ResponseEntity<DataResponse> createHotel(@RequestBody HotelDto hotelDto) {
        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse dataResponse = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Hotels was created successfully",
                hotelService.createHotel(hotelDto)
        );

        return new ResponseEntity<>(dataResponse, httpStatus);
    }

    @GetMapping()
    public ResponseEntity<DataResponse> getHotels() {
        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse dataResponse = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Hotels was got successfully",
                hotelService.getHotels()
        );

        return new ResponseEntity<>(dataResponse, httpStatus);
    }

    @PostMapping("/getHasAvailableRooms")
    public ResponseEntity<DataResponse> getHotelsHasAvailableRooms(@RequestBody(required = false) SearchHotelDto searchHotelDto) {
        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse dataResponse = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Hotels has available rooms was got successfully",
                hotelService.getHotelsHasAvailableRooms(searchHotelDto)
        );

        return new ResponseEntity<>(dataResponse, httpStatus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse> getHotelById(@PathVariable UUID id) {
        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse dataResponse = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Hotel was got successfully",
                hotelService.getHotelById(id)
        );

        return new ResponseEntity<>(dataResponse, httpStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse> updateHotel(@PathVariable UUID id, @RequestBody HotelDto hotelDto) {
        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse dataResponse = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Hotel was updated successfully",
                hotelService.updateHotel(id, hotelDto)
        );

        return new ResponseEntity<>(dataResponse, httpStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse> deleteHotelById(@PathVariable UUID id) {
        hotelService.deleteHotelById(id);

        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse dataResponse = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Hotel was deleted successfully"
        );

        return new ResponseEntity<>(dataResponse, httpStatus);
    }
}
