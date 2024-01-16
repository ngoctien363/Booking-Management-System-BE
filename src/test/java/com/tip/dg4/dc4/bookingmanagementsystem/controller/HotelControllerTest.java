package com.tip.dg4.dc4.bookingmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tip.dg4.dc4.bookingmanagementsystem.controllers.AuthenticationController;
import com.tip.dg4.dc4.bookingmanagementsystem.controllers.HotelController;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.JWTAuthenticationResponseDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.SignInRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.HotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.SearchHotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.BadRequestException;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.NotFoundException;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Hotel;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.Destination;
import com.tip.dg4.dc4.bookingmanagementsystem.services.BookingHistoryService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.HotelService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.res.DataResponse;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HotelControllerTest {
    private static final String PATH_API = "/hotels";
    private static String ADMIN_BEARER_TOKEN = "Bearer ";
    private static List<HotelDto> hotels = new ArrayList<>();
    private static List<UUID> hotelIds = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthenticationController authController;
    @Autowired
    private BookingHistoryService bookingHistoryService;
    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private HotelService hotelService;
    @SpyBean
    private HotelController hotelController;

    @BeforeAll
    public void setUp() {
        //  Setup admin bearer token
        DataResponse adminResponse = authController.signIn(
                new SignInRequestDto("admin@gmail.com", "Admin")
        ).getBody();
        if (adminResponse == null) {
            throw new NullPointerException("Cannot get tokens because data response was null.");
        }
        JWTAuthenticationResponseDto jwt = (JWTAuthenticationResponseDto) adminResponse.getData();
        ADMIN_BEARER_TOKEN += jwt.getToken();

        //  Setup hotel list
        when(hotelService.getHotels()).thenCallRealMethod();
        hotels = hotelService.getHotels();
        hotelIds = hotels.stream().map(HotelDto::getId).toList();
    }

    //  TODO: GET - Get hotels API
    @Test
    public void getHotels_whenValid_thenResponseOk() throws Exception {
        when(hotelService.getHotels()).thenCallRealMethod();
        mockMvc.perform(get(PATH_API)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotels was got successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isArray());
        verify(hotelController, times(1)).getHotels();
    }

    //  TODO: POST - Get hotels has available rooms API
    @Test
    @Transactional
    public void getHotelsHasAvailableRooms_whenEmptyRequestBody_thenResponseOk() throws Exception {
        when(hotelService.getHotelsHasAvailableRooms(null)).thenCallRealMethod();
        mockMvc.perform(post(PATH_API + "/getHasAvailableRooms")
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotels has available rooms was got successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(hotelController, times(1)).getHotelsHasAvailableRooms(null);
    }

    @Test
    @Transactional
    public void getHotelsHasAvailableRooms_whenNonEmptyRequestBody_thenResponseOk() throws Exception {
        SearchHotelDto searchHotelDto = SearchHotelDto.builder()
                .checkInDate(LocalDate.now())
                .checkOutDate(LocalDate.now().plusDays(2))
                .personQuantity(2)
                .destination(Destination.QUY_NHON.getValue()).build();

        when(hotelService.getHotelsHasAvailableRooms(searchHotelDto)).thenCallRealMethod();
        mockMvc.perform(post(PATH_API + "/getHasAvailableRooms")
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchHotelDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotels has available rooms was got successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(hotelController, times(1)).getHotelsHasAvailableRooms(searchHotelDto);
    }

    @Test
    @Transactional
    public void getHotelsHasAvailableRooms_whenEmptyDestination_thenResponseOk() throws Exception {
        SearchHotelDto searchHotelDto = SearchHotelDto.builder()
                .checkInDate(LocalDate.now())
                .checkOutDate(LocalDate.now().plusDays(2))
                .personQuantity(2)
                .destination(null).build();

        when(hotelService.getHotelsHasAvailableRooms(searchHotelDto)).thenCallRealMethod();
        mockMvc.perform(post(PATH_API + "/getHasAvailableRooms")
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchHotelDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotels has available rooms was got successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(hotelController, times(1)).getHotelsHasAvailableRooms(searchHotelDto);
    }

    @Test
    public void getHotelsHasAvailableRooms_whenInvalidCheckDate_thenResponseBadRequest() throws Exception {
        SearchHotelDto searchHotelDto = SearchHotelDto.builder()
                .checkInDate(LocalDate.of(2024, 1, 3))
                .checkOutDate(LocalDate.of(2024, 1, 2))
                .personQuantity(2)
                .destination(null).build();

        doThrow(new BadRequestException("Check-in or check-out date time is invalid")).when(hotelService).getHotelsHasAvailableRooms(searchHotelDto);
        mockMvc.perform(post(PATH_API + "/getHasAvailableRooms")
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchHotelDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Check-in or check-out date time is invalid"));
        verify(hotelController, times(1)).getHotelsHasAvailableRooms(searchHotelDto);
    }

    //  TODO: GET - Get hotel by id API
    @Test
    public void getHotelById_whenValid_thenResponseOk() throws Exception {
        UUID id = hotels.get(0).getId();

        when(hotelService.getHotelById(id)).thenCallRealMethod();
        mockMvc.perform(get(PATH_API + "/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel was got successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(hotelController, times(1)).getHotelById(id);
    }

    @Test
    public void getHotelById_whenInvalid_thenResponseNotFound() throws Exception {
        UUID id = getUUID();

        doThrow(new NotFoundException("Hotel not found with id: " + id)).when(hotelService).getHotelById(id);
        mockMvc.perform(get(PATH_API + "/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel not found with id: " + id));
        verify(hotelController, times(1)).getHotelById(id);
    }

    //  TODO: PUT - Update hotel API
    @Test
    public void updateHotel_whenValid_thenResponseOk() throws Exception {
        HotelDto hotel = hotels.get(0);
        hotel.setDescription("Test changes to description");

        when(hotelService.updateHotel(hotel.getId(), hotel)).thenCallRealMethod();
        mockMvc.perform(put(PATH_API + "/{id}", hotel.getId())
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel was updated successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(hotelController, times(1)).updateHotel(hotel.getId(), hotel);
    }

    @Test
    public void updateHotel_whenInvalidId_thenResponseNotFound() throws Exception {
        UUID hotelId = getUUID();
        HotelDto hotel = hotels.get(0);

        when(hotelService.updateHotel(hotel.getId(), hotel)).thenCallRealMethod();
        mockMvc.perform(put(PATH_API + "/{id}", hotelId)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotel)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel not found with id: " + hotelId));
        verify(hotelController, times(1)).updateHotel(hotelId, hotel);
    }

    @Test
    public void updateHotel_whenExistsNameAndAddress_thenResponseBadRequest() throws Exception {
        HotelDto hotel = hotels.get(0);
        HotelDto anotherHotel = hotels.get(1);
        hotel.setName(anotherHotel.getName());
        hotel.setAddress(anotherHotel.getAddress());

        doThrow(new BadRequestException("Hotel already exists with name and address")).when(hotelService).updateHotel(hotel.getId(), hotel);
        mockMvc.perform(put(PATH_API + "/{id}", hotel.getId())
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotel)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel already exists with name and address"));
        verify(hotelController, times(1)).updateHotel(hotel.getId(), hotel);
    }

    @Test
    public void updateHotel_whenInvalidProperty_thenResponseBadRequest() throws Exception {
        HotelDto hotel = hotels.get(0);
        hotel.setName(null);
        hotel.setAddress(null);

        doThrow(new BadRequestException("Some fields are not allowed to be empty")).when(hotelService).updateHotel(hotel.getId(), hotel);
        mockMvc.perform(put(PATH_API + "/{id}", hotel.getId())
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotel)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Some fields are not allowed to be empty"));
        verify(hotelController, times(1)).updateHotel(hotel.getId(), hotel);
    }

    //  TODO: DELETE - Delete hotel by id API
    @Test
    public void deleteHotelById_whenValid_thenResponseOk() throws Exception {
        UUID id = hotels.parallelStream().filter(hotelDto -> {
            Hotel hotel = hotelService.findById(hotelDto.getId()).orElse(null);

            return hotel != null && !bookingHistoryService.existsByHotel(hotel);
        }).findFirst().map(HotelDto::getId).orElse(null);
        if (id == null) return;

        doNothing().when(hotelService).deleteHotelById(id);
        mockMvc.perform(delete(PATH_API + "/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel was deleted successfully"))
                .andExpect(jsonPath("$.data").isMap());
        verify(hotelController, times(1)).deleteHotelById(id);
    }

    @Test
    public void deleteHotelById_whenInvalidId_thenResponseNotFound() throws Exception {
        UUID id = getUUID();

        doThrow(new NotFoundException("Hotel not found with id: " + id)).when(hotelService).deleteHotelById(id);
        mockMvc.perform(delete(PATH_API + "/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel not found with id: " + id));
        verify(hotelController, times(1)).deleteHotelById(id);
    }

    @Test
    public void deleteHotelById_whenHotelHasHistory_thenResponseOk() throws Exception {
        UUID id = hotels.parallelStream().filter(hotelDto -> {
            Hotel hotel = hotelService.findById(hotelDto.getId()).orElse(null);

            return hotel != null && bookingHistoryService.existsByHotel(hotel);
        }).findFirst().map(HotelDto::getId).orElse(null);
        if (id == null) return;

        doThrow(new BadRequestException("Hotel is living in history")).when(hotelService).deleteHotelById(id);
        mockMvc.perform(delete(PATH_API + "/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel is living in history"));
        verify(hotelController, times(1)).deleteHotelById(id);
    }

    private UUID getUUID() {
        UUID id = UUID.randomUUID();
        while (hotelIds.contains(id)) id = UUID.randomUUID();

        return id;
    }

    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(AppConstant.DATETIME_FORMAT));
    }
}
