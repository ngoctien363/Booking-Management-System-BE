package com.tip.dg4.dc4.bookingmanagementsystem.dto.room;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoomStatusDto {
    private UUID id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstant.DATE_FORMAT)
    private LocalDate checkInDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstant.DATE_FORMAT)
    private LocalDate checkOutDate;

    private int personNumber;
}
