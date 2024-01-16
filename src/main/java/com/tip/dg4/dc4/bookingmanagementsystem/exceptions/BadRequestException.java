package com.tip.dg4.dc4.bookingmanagementsystem.exceptions;

import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

@Log4j2
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
        log.error(Arrays.toString(this.getStackTrace()));
    }
}
