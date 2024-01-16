package com.tip.dg4.dc4.bookingmanagementsystem.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum BookingStatus {
    BOOKED("Đã đặt"),
    USING("Đang sử dụng"),
    CANCELLED("Đã huỷ"),
    PAID("Đã thanh toán"),
    UNDEFINED("Undefined");

    private final String value;

    public static BookingStatus getStatus(String value) {
        return Arrays.stream(BookingStatus.values())
                .filter(bookingStatus -> value.equalsIgnoreCase(bookingStatus.value) || value.equals(String.valueOf(bookingStatus)))
                .findFirst()
                .orElse(UNDEFINED);
    }

    public static String getValue(BookingStatus status) {
        return Arrays.stream(BookingStatus.values())
                .filter(status::equals)
                .findFirst()
                .orElse(UNDEFINED).value;
    }
}
