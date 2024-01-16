package com.tip.dg4.dc4.bookingmanagementsystem.mappers;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.room.RoomStatusDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.RoomStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class RoomStatusMapper {
    public RoomStatus convertDtoToModel(RoomStatusDto roomStatusDto) {
        return RoomStatus.builder()
                .id(roomStatusDto.getId())
                .checkInDate(roomStatusDto.getCheckInDate())
                .checkOutDate(roomStatusDto.getCheckOutDate())
                .build();
    }

    public RoomStatusDto convertModelToDTO(RoomStatus roomStatus) {
        return RoomStatusDto.builder()
                .id(roomStatus.getId())
                .checkInDate(roomStatus.getCheckInDate())
                .checkOutDate(roomStatus.getCheckOutDate())
                .build();
    }

    public List<RoomStatusDto> convertModelsToDTOs(List<RoomStatus> roomStatuses) {
        return Optional.ofNullable(roomStatuses).orElse(Collections.emptyList())
                .stream()
                .map(this::convertModelToDTO)
                .toList();
    }

    public List<RoomStatus> convertDTOsToModels(List<RoomStatusDto> roomStatusDTOs) {
        return Optional.ofNullable(roomStatusDTOs).orElse(Collections.emptyList())
                .stream()
                .map(this::convertDtoToModel)
                .toList();
    }
}
