package com.tip.dg4.dc4.bookingmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tip.dg4.dc4.bookingmanagementsystem.controllers.RoomController;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.SignInDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.HotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.room.RoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.room.SearchRoomDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.RoomType;
import com.tip.dg4.dc4.bookingmanagementsystem.services.AuthenticationService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.HotelService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.RoomService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.RoomStatusService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.tip.dg4.dc4.bookingmanagementsystem.utils.TestUtil.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class RoomControllerTest {
    private static final String PATH_API = "/rooms";
    private static String adminBearerToken = AppConstant.EMPTY;
    private static List<RoomDto> rooms = new ArrayList<>();
    private static List<UUID> roomIds = new ArrayList<>();
    private static List<UUID> hotelIds = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private AuthenticationService authService;
    @SpyBean
    private RoomService roomService;
    @SpyBean
    private RoomStatusService roomStatusService;
    @SpyBean
    private HotelService hotelService;
    @SpyBean
    private RoomController roomController;

    @BeforeAll
    public void setUpAll() {
        //  Setup admin bearer token
        SignInDto adminSignIn = new SignInDto("admin@gmail.com", "Admin");
        when(authService.signIn(adminSignIn)).thenCallRealMethod();
        adminBearerToken = getBearerToken(adminSignIn);

        //  Setup hotel's lists
        when(hotelService.getHotels()).thenCallRealMethod();
        hotelIds = hotelService.getHotels().stream().map(HotelDto::getId).toList();

        //  Setup room's list
        when(roomService.getRoomsByHotelId(hotelIds.get(0))).thenCallRealMethod();
        rooms = roomService.getRoomsByHotelId(hotelIds.get(0));
        roomIds = rooms.stream().map(RoomDto::getId).toList();
    }

    private List<RoomDto> mockRoomDTOs() {
        return Arrays.asList(
                RoomDto.builder().id(UUID.randomUUID()).roomNumber("1001").convenient("Room's convenient").description("Room's description").roomType(RoomType.SINGLE_ROOM.getValue()).isActive(true).build(),
                RoomDto.builder().id(UUID.randomUUID()).roomNumber("1002").convenient("Room's convenient").description("Room's description").roomType(RoomType.SINGLE_ROOM.getValue()).isActive(true).build(),
                RoomDto.builder().id(UUID.randomUUID()).roomNumber("2001").convenient("Room's convenient").description("Room's description").roomType(RoomType.DOUBLE_ROOM.getValue()).isActive(true).build(),
                RoomDto.builder().id(UUID.randomUUID()).roomNumber("2002").convenient("Room's convenient").description("Room's description").roomType(RoomType.DOUBLE_ROOM.getValue()).isActive(true).build(),
                RoomDto.builder().id(UUID.randomUUID()).roomNumber("3001").convenient("Room's convenient").description("Room's description").roomType(RoomType.FAMILY_ROOM.getValue()).isActive(true).build(),
                RoomDto.builder().id(UUID.randomUUID()).roomNumber("3002").convenient("Room's convenient").description("Room's description").roomType(RoomType.FAMILY_ROOM.getValue()).isActive(true).build(),
                RoomDto.builder().id(UUID.randomUUID()).roomNumber("4001").convenient("Room's convenient").description("Room's description").roomType(RoomType.SUITE_ROOM.getValue()).isActive(true).build(),
                RoomDto.builder().id(UUID.randomUUID()).roomNumber("4002").convenient("Room's convenient").description("Room's description").roomType(RoomType.SUITE_ROOM.getValue()).isActive(true).build(),
                RoomDto.builder().id(UUID.randomUUID()).roomNumber("5001").convenient("Room's convenient").description("Room's description").roomType(RoomType.SPECIAL_ROOM.getValue()).isActive(true).build(),
                RoomDto.builder().id(UUID.randomUUID()).roomNumber("5002").convenient("Room's convenient").description("Room's description").roomType(RoomType.SPECIAL_ROOM.getValue()).isActive(true).build()
        );
    }

    //  TODO: POST - Create rooms API
    @Test
    public void createRooms_whenValid_thenResponseOk() throws Exception {
        if (ObjectUtils.isEmpty(hotelIds)) return;

        UUID hotelId = hotelIds.get(0);
        List<RoomDto> mockRoomDTOs = mockRoomDTOs();
        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(post(PATH_API + "/{hotelId}", hotelId)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRoomDTOs)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Room(s) was created successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isArray());
        verify(roomController, times(1)).createRooms(hotelId, mockRoomDTOs);
    }

    @Test
    public void createRooms_whenInvalidHotelId_thenResponseNotFound() throws Exception {
        UUID hotelId = getUUID(hotelIds);
        List<RoomDto> mockRoomDTOs = mockRoomDTOs();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        mockMvc.perform(post(PATH_API + "/{hotelId}", hotelId)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRoomDTOs)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel not found with id: " + hotelId));
        verify(roomController, times(1)).createRooms(hotelId, mockRoomDTOs);
    }

    //  TODO: GET - Get rooms by hotel id API
    @Test
    public void getRoomsByHotelId_whenValid_thenResponseOk() throws Exception {
        if (ObjectUtils.isEmpty(hotelIds)) return;

        UUID hotelId = hotelIds.get(0);
        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(get(PATH_API + "/{hotelId}", hotelId)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Room(s) was got successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isArray());
        verify(roomController, times(1)).getRoomsByHotelId(hotelId);
    }

    @Test
    public void getRoomsByHotelId_whenInvalidHotelId_thenResponseNotFound() throws Exception {
        if (ObjectUtils.isEmpty(hotelIds)) return;

        UUID hotelId = getUUID(hotelIds);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        mockMvc.perform(get(PATH_API + "/{hotelId}", hotelId)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel not found with id: " + hotelId));
        verify(roomController, times(1)).getRoomsByHotelId(hotelId);
    }

    // TODO: POST - Get available rooms by hotel id API
    @Test
    public void getAvailableRoomsByHotelId_whenValid_thenResponseOk() throws Exception {
        if (ObjectUtils.isEmpty(hotelIds)) return;

        UUID hotelId = hotelIds.get(0);
        SearchRoomDto searchRoomDto = SearchRoomDto.builder()
                .checkInDate(LocalDate.now())
                .checkOutDate(LocalDate.now().plusDays(2))
                .personQuantity(3)
                .build();
        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(post(PATH_API + "/getAvailableRooms/{hotelId}", hotelId)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRoomDto)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Room(s) was got successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isArray());
        verify(roomController, times(1)).getAvailableRoomsByHotelId(hotelId, searchRoomDto);
    }

    @Test
    public void getAvailableRoomsByHotelId_whenInvalidSearchRoomDto_thenResponseBadRequest() throws Exception {
        if (ObjectUtils.isEmpty(hotelIds)) return;

        UUID hotelId = hotelIds.get(0);
        SearchRoomDto searchRoomDto = SearchRoomDto.builder()
                .checkInDate(LocalDate.now().plusDays(2))
                .checkOutDate(LocalDate.now())
                .personQuantity(3)
                .build();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        mockMvc.perform(post(PATH_API + "/getAvailableRooms/{hotelId}", hotelId)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRoomDto)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Check-in or check-out date time is invalid"));
        verify(roomController, times(1)).getAvailableRoomsByHotelId(hotelId, searchRoomDto);
    }

    //  TODO: PUT - Update rooms by hotel id
    @Test
    public void updateRoomByHotelId_whenValid_thenResponseOk() throws Exception {
        if (ObjectUtils.isEmpty(hotelIds)) return;
        if (ObjectUtils.isEmpty(rooms)) return;

        UUID hotelId = hotelIds.get(0);
        RoomDto roomDto = rooms.get(0);
        roomDto.setActive(false);
        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(put(PATH_API + "/{hotelId}", hotelId)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomDto)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Room was updated successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(roomController, times(1)).updateRoomByHotelId(hotelId, roomDto);
    }

    @Test
    public void updateRoomByHotelId_whenInvalidRoomDto_thenResponseBadRequest() throws Exception {
        if (ObjectUtils.isEmpty(hotelIds)) return;
        if (ObjectUtils.isEmpty(rooms) || rooms.size() < 2) return;

        UUID hotelId = hotelIds.get(0);
        RoomDto roomDto = rooms.get(0);
        roomDto.setRoomNumber(rooms.get(1).getRoomNumber());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        mockMvc.perform(put(PATH_API + "/{hotelId}", hotelId)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomDto)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Room already exists with room number in this hotel"));
        verify(roomController, times(1)).updateRoomByHotelId(hotelId, roomDto);
    }

    //  TODO: DELETE - Delete room by id API
    @Test
    public void deleteRoomById_whenValid_thenResponseOk() throws Exception {
        if (ObjectUtils.isEmpty(roomIds)) return;

        UUID roomId = roomIds.stream().filter(roomIds -> !isBookedRoom(roomIds)).findFirst().orElse(null);
        if (roomId == null) return;

        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(delete(PATH_API + "/{id}", roomId)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Room was deleted successfully"));
        verify(roomController, times(1)).deleteRoomById(roomId);
    }

    @Test
    public void deleteRoomById_whenBookedRoom_thenResponseBadRequest() throws Exception {
        if (ObjectUtils.isEmpty(roomIds)) return;

        UUID roomId = roomIds.stream().filter(this::isBookedRoom).findFirst().orElse(null);
        if (roomId == null) return;

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        mockMvc.perform(delete(PATH_API + "/{id}", roomId)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Room is scheduled for use"));
        verify(roomController, times(1)).deleteRoomById(roomId);
    }

    private boolean isBookedRoom(UUID roomId) {
        return roomStatusService.existsByRoomId(roomId);
    }
}
