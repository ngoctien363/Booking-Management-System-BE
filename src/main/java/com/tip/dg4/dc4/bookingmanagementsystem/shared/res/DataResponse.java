package com.tip.dg4.dc4.bookingmanagementsystem.shared.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataResponse {
    private String status;
    private String message;
    private Object data;

    public DataResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.data = new HashMap<>();
    }
}
