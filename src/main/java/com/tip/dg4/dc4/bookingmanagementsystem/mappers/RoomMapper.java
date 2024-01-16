package com.tip.dg4.dc4.bookingmanagementsystem.mappers;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.room.RoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Room;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomMapper {
    private final RoomStatusMapper roomStatusMapper;

    public Room convertDtoToModel(RoomDto roomDto) {
        return Room.builder()
                .id(roomDto.getId())
                .roomNumber(roomDto.getRoomNumber())
                .convenient(roomDto.getConvenient())
                .description(roomDto.getDescription())
                .type(RoomType.getType(roomDto.getRoomType()))
                .isActive(roomDto.isActive())
                .build();
    }

    public RoomDto convertModelToDTO(Room room) {
        RoomType roomType = room.getType();

        return RoomDto.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .price(roomType.getPriceOneNight())
                .convenient(room.getConvenient())
                .description(room.getDescription())
                .maxPersonNumber(roomType.getMaxPersonNumber())
                .roomType(roomType.getValue())
                .isActive(room.isActive())
                .build();
    }
}
