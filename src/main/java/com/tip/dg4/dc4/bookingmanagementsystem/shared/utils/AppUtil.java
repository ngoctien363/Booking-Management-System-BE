package com.tip.dg4.dc4.bookingmanagementsystem.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppUtil {
    public static int getCorrectPage(int page, int size, long totalElements) {
        int totalPages = (int) Math.max((totalElements / getCorrectSize(size, totalElements)), 0);

        return Math.max(Math.min((page - 1), (totalPages - 1)), 0);
    }

    public static int getCorrectSize(int size, long totalElements) {
        return (int) Math.max(Math.min(size, totalElements), 1);
    }

    public static <E> Page<E> paginateList(List<E> list, Pageable pageable) {
        Page<E> listPages = new PageImpl<>(list, pageable, list.size());

        return Optional.of(listPages).orElse(new PageImpl<>(Collections.emptyList()));
    }

    public static <T> T defaultIfNull(T object, T defaultValue) {
        return object != null ? object : defaultValue;
    }

    public static boolean nonNumeric(String str) {
        return !str.matches("-?\\d+");
    }
}
