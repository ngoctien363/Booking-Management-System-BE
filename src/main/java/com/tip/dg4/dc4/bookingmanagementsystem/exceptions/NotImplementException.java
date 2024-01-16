package com.tip.dg4.dc4.bookingmanagementsystem.exceptions;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class NotImplementException extends RuntimeException {
    public NotImplementException(String message) {
        super(message);
        log.error(this.getStackTrace());
    }
}
