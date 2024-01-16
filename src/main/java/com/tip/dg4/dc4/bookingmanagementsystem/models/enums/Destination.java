package com.tip.dg4.dc4.bookingmanagementsystem.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Destination {
    QUY_NHON("Quy Nhon"),
    DA_NANG("Da Nang"),
    SAI_GON("Sai Gon"),
    UNDEFINED("Undefined");

    private final String value;

    public static Destination getDestination(String value) {
        return Arrays.stream(Destination.values())
                .filter(destination -> value.equalsIgnoreCase(destination.value))
                .findFirst()
                .orElse(UNDEFINED);
    }

    public static String getValue(Destination destination) {
        return Arrays.stream(Destination.values())
                .filter(destination::equals)
                .findFirst()
                .orElse(UNDEFINED).value;
    }
}
