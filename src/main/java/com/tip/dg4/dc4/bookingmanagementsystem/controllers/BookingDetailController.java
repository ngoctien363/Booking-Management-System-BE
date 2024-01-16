package com.tip.dg4.dc4.bookingmanagementsystem.controllers;

import com.tip.dg4.dc4.bookingmanagementsystem.services.BookingDetailService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.res.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/bookingDetails")
@RequiredArgsConstructor
public class BookingDetailController {
    private final BookingDetailService bookingDetailService;

    @GetMapping("/{bookingHistoryId}")
    public ResponseEntity<DataResponse> getByBookingHistoryId(@PathVariable UUID bookingHistoryId) {
        HttpStatus httpStatus = HttpStatus.OK;
        DataResponse result = new DataResponse(
                httpStatus.getReasonPhrase(),
                "Booking detail(s) has been got successfully",
                bookingDetailService.getByBookingHistoryId(bookingHistoryId)
        );

        return new ResponseEntity<>(result, httpStatus);
    }
}
