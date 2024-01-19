package com.tip.dg4.dc4.bookingmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tip.dg4.dc4.bookingmanagementsystem.controllers.BookingHistoryController;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.SignInDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingRoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.HotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.BookingHistory;
import com.tip.dg4.dc4.bookingmanagementsystem.models.User;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.BookingStatus;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.RoomType;
import com.tip.dg4.dc4.bookingmanagementsystem.repositories.BookingHistoryRepository;
import com.tip.dg4.dc4.bookingmanagementsystem.repositories.UserRepository;
import com.tip.dg4.dc4.bookingmanagementsystem.services.AuthenticationService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.HotelService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.*;

import static com.tip.dg4.dc4.bookingmanagementsystem.utils.TestUtil.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class BookingHistoryControllerTest {
    private static final String PATH_API = "/bookingHistories";
    private static String adminBearerToken = AppConstant.EMPTY;
    private static List<UUID> hotelIds = new ArrayList<>();
    private static List<UUID> userIds = new ArrayList<>();
    private static List<UUID> bookingHistoryIds = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private AuthenticationService authService;
    @SpyBean
    private HotelService hotelService;
    @SpyBean
    private BookingHistoryController bookingHistoryController;
    @SpyBean
    private UserRepository userRepository;
    @SpyBean
    private BookingHistoryRepository bookingHistoryRepository;

    @BeforeAll
    public void setUp() {
        //  Setup admin bearer token
        SignInDto adminSignIn = new SignInDto("admin@gmail.com", "Admin");
        when(authService.signIn(adminSignIn)).thenCallRealMethod();
        adminBearerToken = getBearerToken(adminSignIn);

        //  Setup hotel's lists
        when(hotelService.getHotels()).thenCallRealMethod();
        hotelIds = hotelService.getHotels().stream().map(HotelDto::getId).toList();

        //  Setup user's lists
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        userIds = userRepository.findAll().stream().map(User::getId).toList();

        //  Setup Booking history's lists
        when(bookingHistoryRepository.findAll()).thenReturn(new ArrayList<>());
        bookingHistoryIds = bookingHistoryRepository.findAll().stream().map(BookingHistory::getId).toList();
    }

    private LinkedHashMap<String, Integer> mockRooms() {
        return new LinkedHashMap<>(Map.of(
                RoomType.SINGLE_ROOM.getValue(), 1,
                RoomType.DOUBLE_ROOM.getValue(), 2,
                RoomType.FAMILY_ROOM.getValue(), 3
        ));
    }

    // TODO: POST - Booking rooms API
    @Test
    public void bookingRooms_whenValid_thenResponseOk() throws Exception {
        if (ObjectUtils.isEmpty(hotelIds) || ObjectUtils.isEmpty(userIds)) return;

        BookingRoomDto bookingRoomDto = BookingRoomDto.builder()
                .hotelId(hotelIds.get(0))
                .userId(userIds.get(0))
                .checkInDate(LocalDate.now())
                .checkOutDate(LocalDate.now().plusDays(2))
                .personQuantity(3)
                .rooms(mockRooms()).build();
        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(post(PATH_API)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRoomDto)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Room has been booked successfully"))
                .andExpect(jsonPath("$.data").isMap());
        verify(bookingHistoryController, times(1)).bookingRooms(bookingRoomDto);
    }

    @Test
    public void bookingRooms_whenInvalidBookingRoomDto_thenResponseBadRequest() throws Exception {
        if (ObjectUtils.isEmpty(hotelIds) || ObjectUtils.isEmpty(userIds)) return;

        BookingRoomDto bookingRoomDto = BookingRoomDto.builder()
                .hotelId(hotelIds.get(0))
                .userId(userIds.get(0))
                .checkInDate(LocalDate.now().plusDays(2))
                .checkOutDate(LocalDate.now())
                .personQuantity(3)
                .rooms(mockRooms()).build();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        mockMvc.perform(post(PATH_API)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRoomDto)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Check-in or check-out date time is invalid"));
        verify(bookingHistoryController, times(1)).bookingRooms(bookingRoomDto);
    }

    //  TODO: Get booking histories API
    @Test
    public void getBookingHistories_whenValid_thenResponseOk() throws Exception {
        if (ObjectUtils.isEmpty(hotelIds) || ObjectUtils.isEmpty(userIds)) return;

        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(get(PATH_API)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Booking histories has been got successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isMap());
        verify(bookingHistoryController, times(1)).getBookingHistories();
    }

    //  TODO: Get by user id and status API
    @Test
    public void getByUserIdAndStatus_whenValid_thenResponseOk() throws Exception {
        if (ObjectUtils.isEmpty(hotelIds) || ObjectUtils.isEmpty(userIds)) return;

        UUID userId = userIds.get(0);
        BookingStatus bookingStatus = BookingStatus.BOOKED;
        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(get(PATH_API + "/{userId}/{status}", userId, bookingStatus)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Booking histories has been got successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isMap());
        verify(bookingHistoryController, times(1)).getByUserIdAndStatus(userId, bookingStatus);
    }

    @Test
    public void getByUserIdAndStatus_whenInvalidUserId_thenResponseOk() throws Exception {
        if (ObjectUtils.isEmpty(hotelIds) || ObjectUtils.isEmpty(userIds)) return;

        UUID userId = getUUID(userIds);
        BookingStatus bookingStatus = BookingStatus.BOOKED;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        mockMvc.perform(get(PATH_API + "/{userId}/{status}", userId, bookingStatus)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Booking histories has been got successfully"));
        verify(bookingHistoryController, times(1)).getByUserIdAndStatus(userId, bookingStatus);
    }

    //  TODO: PUT: Update status API
    @Test
    public void updateStatus_whenValid_thenResponseOk() throws Exception {
        if (ObjectUtils.isEmpty(bookingHistoryIds)) return;

        UUID bookingHistoryId = bookingHistoryIds.get(0);
        BookingStatus bookingStatus = BookingStatus.BOOKED;
        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(get(PATH_API + "/{id}/{status}", bookingHistoryId, bookingStatus)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Status was updated successfully"))
                .andExpect(jsonPath("$.data").isMap());
        verify(bookingHistoryController, times(1)).getByUserIdAndStatus(bookingHistoryId, bookingStatus);
    }

    @Test
    public void updateStatus_whenInvalidId_thenResponseNotFound() throws Exception {
        if (ObjectUtils.isEmpty(bookingHistoryIds)) return;

        UUID bookingHistoryId = getUUID(bookingHistoryIds);
        BookingStatus bookingStatus = BookingStatus.BOOKED;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        mockMvc.perform(get(PATH_API + "/{id}/{status}", bookingHistoryId, bookingStatus)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Booking history does not found by id: " + bookingHistoryId));
        verify(bookingHistoryController, times(1)).getByUserIdAndStatus(bookingHistoryId, bookingStatus);
    }
}
