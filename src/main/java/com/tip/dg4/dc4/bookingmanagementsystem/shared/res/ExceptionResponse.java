package com.tip.dg4.dc4.bookingmanagementsystem.shared.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstant.DATETIME_FORMAT)
    private LocalDateTime timestamp;
    private String status;
    private String message;
}
