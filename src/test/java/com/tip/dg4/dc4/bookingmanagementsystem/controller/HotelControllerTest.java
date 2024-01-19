package com.tip.dg4.dc4.bookingmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tip.dg4.dc4.bookingmanagementsystem.controllers.HotelController;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.SignInDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.HotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.hotel.SearchHotelDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.Hotel;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.Destination;
import com.tip.dg4.dc4.bookingmanagementsystem.services.AuthenticationService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.BookingHistoryService;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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
public class HotelControllerTest {
    private static final String PATH_API = "/hotels";
    private static String adminBearerToken = AppConstant.EMPTY;
    private static List<HotelDto> hotels = new ArrayList<>();
    private static List<UUID> hotelIds = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookingHistoryService bookingHistoryService;
    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private AuthenticationService authService;
    @SpyBean
    private HotelService hotelService;
    @SpyBean
    private HotelController hotelController;

    @BeforeAll
    public void setUp() {
        //  Setup admin bearer token
        SignInDto adminSignIn = new SignInDto("admin@gmail.com", "Admin");
        when(authService.signIn(adminSignIn)).thenCallRealMethod();
        adminBearerToken = getBearerToken(adminSignIn);

        //  Setup hotel's lists
        when(hotelService.getHotels()).thenCallRealMethod();
        hotels = hotelService.getHotels();
        hotelIds = hotels.stream().map(HotelDto::getId).toList();
    }

    //  TODO: GET - Get hotels API
    @Test
    public void getHotels_whenValid_thenResponseOk() throws Exception {
        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(get(PATH_API)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotels was got successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data").isArray());
        verify(hotelController, times(1)).getHotels();
    }

    //  TODO: POST - Get hotels has available rooms API
    @Test
    public void getHotelsHasAvailableRooms_whenEmptyRequestBody_thenResponseOk() throws Exception {
        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(post(PATH_API + "/getHasAvailableRooms")
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotels has available rooms was got successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(hotelController, times(1)).getHotelsHasAvailableRooms(null);
    }

    @Test
    public void getHotelsHasAvailableRooms_whenNonEmptyRequestBody_thenResponseOk() throws Exception {
        SearchHotelDto searchHotelDto = SearchHotelDto.builder()
                .checkInDate(LocalDate.now())
                .checkOutDate(LocalDate.now().plusDays(2))
                .personQuantity(2)
                .destination(Destination.QUY_NHON.getValue()).build();

        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(post(PATH_API + "/getHasAvailableRooms")
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchHotelDto)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotels has available rooms was got successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(hotelController, times(1)).getHotelsHasAvailableRooms(searchHotelDto);
    }

    @Test
    public void getHotelsHasAvailableRooms_whenEmptyDestination_thenResponseOk() throws Exception {
        SearchHotelDto searchHotelDto = SearchHotelDto.builder()
                .checkInDate(LocalDate.now())
                .checkOutDate(LocalDate.now().plusDays(2))
                .personQuantity(2)
                .destination(null).build();

        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(post(PATH_API + "/getHasAvailableRooms")
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchHotelDto)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
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

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        mockMvc.perform(post(PATH_API + "/getHasAvailableRooms")
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchHotelDto)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Check-in or check-out date time is invalid"));
        verify(hotelController, times(1)).getHotelsHasAvailableRooms(searchHotelDto);
    }

    //  TODO: GET - Get hotel by id API
    @Test
    public void getHotelById_whenValid_thenResponseOk() throws Exception {
        UUID id = hotels.get(0).getId();

        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(get(PATH_API + "/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel was got successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(hotelController, times(1)).getHotelById(id);
    }

    @Test
    public void getHotelById_whenInvalid_thenResponseNotFound() throws Exception {
        UUID id = getUUID(hotelIds);

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        mockMvc.perform(get(PATH_API + "/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel not found with id: " + id));
        verify(hotelController, times(1)).getHotelById(id);
    }

    //  TODO: PUT - Update hotel API
    @Test
    public void updateHotel_whenValid_thenResponseOk() throws Exception {
        HotelDto hotel = hotels.get(0);
        hotel.setDescription("Test changes to description");

        HttpStatus httpStatus = HttpStatus.OK;
        MvcResult result = mockMvc.perform(put(PATH_API + "/{id}", hotel.getId())
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotel)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel was updated successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andReturn();
        verifyData(result, hotel);
        verify(hotelController, times(1)).updateHotel(hotel.getId(), hotel);
    }

    @Test
    public void updateHotel_whenInvalidId_thenResponseNotFound() throws Exception {
        UUID hotelId = getUUID(hotelIds);
        HotelDto hotel = hotels.get(0);

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        mockMvc.perform(put(PATH_API + "/{id}", hotelId)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotel)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel not found with id: " + hotelId));
        verify(hotelController, times(1)).updateHotel(hotelId, hotel);
    }

    @Test
    public void updateHotel_whenExistsNameAndAddress_thenResponseBadRequest() throws Exception {
        HotelDto hotel = hotels.get(0);
        HotelDto anotherHotel = hotels.get(1);
        hotel.setName(anotherHotel.getName());
        hotel.setAddress(anotherHotel.getAddress());

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        mockMvc.perform(put(PATH_API + "/{id}", hotel.getId())
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotel)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel already exists with name and address"));
        verify(hotelController, times(1)).updateHotel(hotel.getId(), hotel);
    }

    @Test
    public void updateHotel_whenInvalidProperty_thenResponseBadRequest() throws Exception {
        HotelDto hotel = hotels.get(0);
        hotel.setName(null);
        hotel.setAddress(null);

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        mockMvc.perform(put(PATH_API + "/{id}", hotel.getId())
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotel)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
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

        HttpStatus httpStatus = HttpStatus.OK;
        mockMvc.perform(delete(PATH_API + "/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel was deleted successfully"))
                .andExpect(jsonPath("$.data").isMap());
        verify(hotelController, times(1)).deleteHotelById(id);
    }

    @Test
    public void deleteHotelById_whenInvalidId_thenResponseNotFound() throws Exception {
        UUID id = getUUID(hotelIds);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        mockMvc.perform(delete(PATH_API + "/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel not found with id: " + id));
        verify(hotelController, times(1)).deleteHotelById(id);
    }

    @Test
    public void deleteHotelById_whenHotelHasHistory_thenResponseBadRequest() throws Exception {
        UUID id = hotels.parallelStream().filter(hotelDto -> {
            Hotel hotel = hotelService.findById(hotelDto.getId()).orElse(null);

            return hotel != null && bookingHistoryService.existsByHotel(hotel);
        }).findFirst().map(HotelDto::getId).orElse(null);
        if (id == null) return;

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        mockMvc.perform(delete(PATH_API + "/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.timestamp").value(getCurrentDateTime()))
                .andExpect(jsonPath("$.status").value(httpStatus.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Hotel is living in history"));
        verify(hotelController, times(1)).deleteHotelById(id);
    }
}
