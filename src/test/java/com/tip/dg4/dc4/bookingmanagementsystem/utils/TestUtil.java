package com.tip.dg4.dc4.bookingmanagementsystem.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.JWTAuthenticationDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.SignInDto;
import com.tip.dg4.dc4.bookingmanagementsystem.services.AuthenticationService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Component
public class TestUtil {
    private static ObjectMapper objectMapper;
    private static AuthenticationService authService;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        TestUtil.objectMapper = objectMapper;
    }

    @Autowired
    public void setAuthService(AuthenticationService authService) {
        TestUtil.authService = authService;
    }

    public static String getBearerToken(SignInDto signInDto) {
        JWTAuthenticationDto jwt = authService.signIn(signInDto);
        if (jwt == null) {
            throw new NullPointerException("Cannot get tokens because data response was null.");
        }

        return AppConstant.BEARER + jwt.getToken();
    }

    public static void verifyData(MvcResult mvcResult, Object root) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsByteArray()).get("data");
        Object result = objectMapper.treeToValue(jsonNode, root.getClass());

        assertEquals(root, result);
    }

    public static UUID getUUID(List<UUID> ids) {
        UUID id = UUID.randomUUID();
        while (ids.contains(id)) id = UUID.randomUUID();

        return id;
    }

    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(AppConstant.DATETIME_FORMAT));
    }
}
