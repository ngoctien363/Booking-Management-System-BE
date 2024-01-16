package com.tip.dg4.dc4.bookingmanagementsystem.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum RoomType {
    SINGLE_ROOM("Phòng đơn", new BigDecimal(50), 2, "singleRoom"),
    DOUBLE_ROOM("Phòng đôi", new BigDecimal(100), 4, "doubleRoom"),
    FAMILY_ROOM("Phòng gia đình", new BigDecimal(150), 6, "familyRoom"),
    SUITE_ROOM("Phòng thương gia", new BigDecimal(200), 8, "suiteRoom"),
    SPECIAL_ROOM("Phòng đặc biệt", new BigDecimal(300), 12, "specialRoom"),
    UNDEFINED("Undefined", new BigDecimal(0), 0, "undefined");

    private final String value;
    private final BigDecimal priceOneNight;
    private final int maxPersonNumber;
    private final String code;

    public static RoomType getType(String value) {
        return Arrays.stream(RoomType.values())
                .filter(roomType -> value.equalsIgnoreCase(roomType.value) || value.equals(String.valueOf(roomType)))
                .findFirst()
                .orElse(UNDEFINED);
    }

    public static String getValue(RoomType type) {
        return Arrays.stream(RoomType.values())
                .filter(type::equals)
                .findFirst()
                .orElse(UNDEFINED).value;
    }

    public static RoomType getByCode(String code) {
        return Arrays.stream(RoomType.values())
                .filter(roomType -> code.equalsIgnoreCase(roomType.code))
                .findFirst()
                .orElse(UNDEFINED);
    }
}
