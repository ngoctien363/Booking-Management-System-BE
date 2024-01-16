package com.tip.dg4.dc4.bookingmanagementsystem.services.implement;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingDetailDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingRoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.BadRequestException;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.NotFoundException;
import com.tip.dg4.dc4.bookingmanagementsystem.mappers.BookingDetailMapper;
import com.tip.dg4.dc4.bookingmanagementsystem.models.*;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.RoomType;
import com.tip.dg4.dc4.bookingmanagementsystem.repositories.BookingDetailRepository;
import com.tip.dg4.dc4.bookingmanagementsystem.services.BookingDetailService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.BookingHistoryService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.RoomService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.RoomStatusService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.ExceptionConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class BookingDetailServiceImpl implements BookingDetailService {
    private final BookingDetailRepository bookingDetailRepository;
    private final RoomService roomService;
    private final RoomStatusService roomStatusService;
    @Lazy
    private final BookingHistoryService bookingHistoryService;
    private final BookingDetailMapper bookingDetailMapper;

    @Override
    public void create(BookingHistory bookingHistory, Hotel hotel, BookingRoomDto bookingRoomDto) {
        for (Map.Entry<String, Integer> room : bookingRoomDto.getRooms().entrySet()) {
            RoomType roomType = RoomType.getType(room.getKey());
            for (int i = 0; i < room.getValue(); i++) {
                Room availableRoom = roomService
                        .getAvailableRooms(hotel, bookingRoomDto.getCheckInDate(), bookingRoomDto.getCheckOutDate())
                        .stream().filter(r -> r.getType().equals(roomType)).findFirst()
                        .orElseThrow(() -> new BadRequestException(ExceptionConstant.BHISTORY_E001 + roomType.getValue()));
                RoomStatus roomStatus = roomStatusService.create(availableRoom, bookingRoomDto);
                BookingDetail bookingDetail = BookingDetail.builder()
                        .roomNumber(availableRoom.getRoomNumber())
                        .roomType(roomType)
                        .convenient(availableRoom.getConvenient())
                        .price(roomType.getPriceOneNight())
                        .maxPersonNum(roomType.getMaxPersonNumber())
                        .roomStatus(roomStatus)
                        .bookingHistory(bookingHistory).build();

                bookingDetailRepository.save(bookingDetail);
            }
        }
    }

    @Override
    public List<BookingDetailDto> getByBookingHistoryId(UUID bookingHistoryId) {
        BookingHistory bookingHistory = bookingHistoryService.findById(bookingHistoryId)
                .orElseThrow(() -> new NotFoundException(ExceptionConstant.BHISTORY_E002 + bookingHistoryId));

        return bookingDetailRepository.findByBookingHistory(bookingHistory)
                .stream().map(bookingDetailMapper::convertEntityToDto)
                .toList();
    }
}
