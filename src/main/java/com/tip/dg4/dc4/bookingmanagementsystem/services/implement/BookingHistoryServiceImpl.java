package com.tip.dg4.dc4.bookingmanagementsystem.services.implement;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingHistoryDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingRoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.GetAllBookingHistoryDto;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.BadRequestException;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.NotFoundException;
import com.tip.dg4.dc4.bookingmanagementsystem.mappers.BookingHistoryMapper;
import com.tip.dg4.dc4.bookingmanagementsystem.models.BookingHistory;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Hotel;
import com.tip.dg4.dc4.bookingmanagementsystem.models.RoomStatus;
import com.tip.dg4.dc4.bookingmanagementsystem.models.User;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.BookingStatus;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.RoomType;
import com.tip.dg4.dc4.bookingmanagementsystem.repositories.BookingHistoryRepository;
import com.tip.dg4.dc4.bookingmanagementsystem.services.*;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.ExceptionConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingHistoryServiceImpl implements BookingHistoryService {
    private final BookingHistoryRepository bookingHistoryRepository;
    private final HotelService hotelService;
    private final UserService userService;
    private final BookingDetailService bookingDetailService;
    private final RoomStatusService roomStatusService;
    private final ImageService imageService;
    private final BookingHistoryMapper bookingHistoryMapper;

    @Transactional
    @Override
    public void bookingRooms(BookingRoomDto bookingRoomDto) {
        Hotel hotel = hotelService.findById(bookingRoomDto.getHotelId())
                .orElseThrow(() -> new NotFoundException(ExceptionConstant.HOTEL_E002 + bookingRoomDto.getHotelId()));
        User user = userService.findById(bookingRoomDto.getUserId())
                .orElseThrow(() -> new NotFoundException(ExceptionConstant.USER_E001));
        if (bookingRoomDto.getCheckOutDate().isBefore(bookingRoomDto.getCheckInDate())) {
            throw new BadRequestException(ExceptionConstant.ROOM_E005);
        }

        LocalDate currentDate = LocalDate.now();
        int totalPrice = 0;
        for (Map.Entry<String, Integer> room : bookingRoomDto.getRooms().entrySet()) {
            RoomType roomType = RoomType.getType(room.getKey());
            if (RoomType.UNDEFINED.equals(roomType)) {
                throw new BadRequestException(ExceptionConstant.ROOM_E003);
            }
            totalPrice += roomType.getPriceOneNight().intValue() * room.getValue();
        }
        BookingHistory bookingHistory = BookingHistory.builder()
                .checkInDate(bookingRoomDto.getCheckInDate())
                .checkOutDate(bookingRoomDto.getCheckOutDate())
                .status(BookingStatus.BOOKED)
                .bookingDate(currentDate)
                .personQuantity(bookingRoomDto.getPersonQuantity())
                .totalPrice(new BigDecimal(totalPrice))
                .user(user)
                .hotel(hotel).build();

        bookingHistoryRepository.save(bookingHistory);
        bookingDetailService.create(bookingHistory, hotel, bookingRoomDto);
    }

    @Override
    public List<GetAllBookingHistoryDto> getBookingHistories() {
        return bookingHistoryRepository.findAll().stream().map(bookingHistoryMapper::convertEntityToDto).toList();
    }

    @Override
    public List<BookingHistoryDto> getByUserId(UUID userId, BookingStatus status) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionConstant.USER_E001));
        BookingStatus bookingStatus = BookingStatus.getStatus(String.valueOf(status));
        List<BookingHistory> bookingHistories = BookingStatus.UNDEFINED.equals(bookingStatus) ?
                bookingHistoryRepository.findByUser(user) :
                bookingHistoryRepository.findByUser(user).stream()
                        .filter(bookingHistory -> bookingHistory.getStatus().equals(status)).toList();

        return bookingHistories.stream().map(bookingHistory -> {
            String imageURL = imageService.getImagesByObjectId(bookingHistory.getHotel().getId()).get(0);

            return bookingHistoryMapper.convertEntityToDto(bookingHistory, imageURL);
        }).toList();
    }

    @Transactional
    @Override
    public void updateStatus(UUID id, BookingStatus status) {
        BookingHistory bookingHistory = bookingHistoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionConstant.BHISTORY_E002 + id));

        if (BookingStatus.CANCELLED.equals(bookingHistory.getStatus()) ||
                BookingStatus.PAID.equals(bookingHistory.getStatus())) {
            throw new BadRequestException(ExceptionConstant.BHISTORY_E003);
        }
        if (BookingStatus.USING.equals(bookingHistory.getStatus())
                && BookingStatus.BOOKED.equals(status)) {
            throw new BadRequestException(ExceptionConstant.BHISTORY_E004);
        }

        bookingHistory.setStatus(status);
        if (BookingStatus.PAID.equals(status)) {
            bookingHistory.setPaymentDate(LocalDate.now());
        }
        bookingHistoryRepository.save(bookingHistory);
        if (BookingStatus.CANCELLED.equals(status) || BookingStatus.PAID.equals(status)) {
            bookingHistory.getBookingDetails().forEach(bookingDetail -> {
                RoomStatus roomStatus = bookingDetail.getRoomStatus();
                if (roomStatus != null) {
                    bookingDetail.setRoomStatus(null);
                    roomStatusService.delete(roomStatus);
                }
            });
        }
    }

    @Override
    public Optional<BookingHistory> findById(UUID id) {
        return bookingHistoryRepository.findById(id);
    }

    @Override
    public boolean existsByHotel(Hotel hotel) {
        return bookingHistoryRepository.existsByHotel(hotel);
    }
}
