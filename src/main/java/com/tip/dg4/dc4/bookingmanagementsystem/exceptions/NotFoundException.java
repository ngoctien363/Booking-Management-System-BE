package com.tip.dg4.dc4.bookingmanagementsystem.exceptions;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
        log.error(this.getStackTrace());
    }
}
