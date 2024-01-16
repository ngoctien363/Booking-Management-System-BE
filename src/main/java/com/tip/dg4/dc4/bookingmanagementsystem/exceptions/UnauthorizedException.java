package com.tip.dg4.dc4.bookingmanagementsystem.exceptions;

import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

@Log4j2
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
        log.error(Arrays.toString(this.getStackTrace()));
    }
}
