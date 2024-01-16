package com.tip.dg4.dc4.bookingmanagementsystem.controllers;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingRoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.BookingStatus;
import com.tip.dg4.dc4.bookingmanagementsystem.services.BookingHistoryService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.res.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bookingHistories")
@RequiredArgsConstructor
public class BookingHistoryController {
    private final BookingHistoryService bookingHistoryService;

    @PostMapping("/bookingRooms")
    public ResponseEntity<DataResponse> bookingRooms(@RequestBody BookingRoomDto bookingRoomDto) {
        bookingHistoryService.bookingRooms(bookingRoomDto);

        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse result = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Room has been booked successfully"
        );

        return new ResponseEntity<>(result, httpStatus);
    }

    @GetMapping("")
    public ResponseEntity<DataResponse> getBookingHistories() {
        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse result = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Booking histories has been got successfully",
                bookingHistoryService.getBookingHistories()
        );

        return new ResponseEntity<>(result, httpStatus);
    }

    @GetMapping("/{userId}/{status}")
    public ResponseEntity<DataResponse> getByUserId(@PathVariable UUID userId, @PathVariable(required = false) BookingStatus status) {
        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse result = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Booking histories has been got successfully",
                bookingHistoryService.getByUserId(userId, status)
        );

        return new ResponseEntity<>(result, httpStatus);
    }

    @PutMapping("/{id}/{status}")
    public ResponseEntity<DataResponse> updateStatus(@PathVariable UUID id, @PathVariable BookingStatus status) {
        bookingHistoryService.updateStatus(id, status);

        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse result = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Status was updated successfully"
        );

        return new ResponseEntity<>(result, httpStatus);
    }
}
