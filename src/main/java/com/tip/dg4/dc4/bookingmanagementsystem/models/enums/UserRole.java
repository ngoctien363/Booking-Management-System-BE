package com.tip.dg4.dc4.bookingmanagementsystem.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ADMIN("Admin"),
    USER("User"),
    UNDEFINED("Undefined");

    private final String value;

    public static UserRole getRole(String value) {
        return Arrays.stream(UserRole.values())
                .filter(role -> value.equalsIgnoreCase(role.value))
                .findFirst()
                .orElse(UNDEFINED);
    }

    public static String getValue(UserRole role) {
        return Arrays.stream(UserRole.values())
                .filter(role::equals)
                .findFirst()
                .orElse(UNDEFINED).value;
    }
}
