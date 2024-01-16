package com.tip.dg4.dc4.bookingmanagementsystem.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.ChangePasswordRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.ForgotPasswordRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.SignInRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.SignUpRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * AuthenticationControllerTest
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 1/3/2024
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void givenSignUpURI_whenMockMVC_thenVerifyResponse() throws Exception {
		SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.name("ABC")
				.surname("ABC")
				.password("ABC")
				.phone("1233211233")
				.isActive(true)
				.build();
		mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpRequestDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.email").value("abcsdfffemmggmmllhhlllkkk@gmail.com"))
				.andExpect(jsonPath("$.data.name").value("ABC"))
				.andExpect(jsonPath("$.data.surname").value("ABC"))
				.andExpect(jsonPath("$.data.phone").value("1233211233"))
				.andExpect(jsonPath("$.data.role").value("USER"))
				.andExpect(jsonPath("$.data.active").value(true))
				.andExpect(jsonPath("$.message").value("User was created successfully"));
	}

	@Test
	public void givenSignInURI_whenMockMVC_thenVerifyResponse() throws Exception {
		SignInRequestDto signInRequestDto = SignInRequestDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.password("ABC")
				.build();
		mockMvc.perform(post("/auth/signin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signInRequestDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.userName").value("abcsdfffemmggmmllhhlllkkk@gmail.com"))
				.andExpect(jsonPath("$.data.name").value("ABC"))
				.andExpect(jsonPath("$.data.surname").value("ABC"))
				.andExpect(jsonPath("$.data.role").value("USER"))
				.andExpect(jsonPath("$.message").value("Sign in successfully"));
	}

	@Test
	public void givenChangePasswordURI_whenMockMVC_thenVerifyResponse() throws Exception {
		ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
				.oldPassword("ABC")
				.newPassword("ABC123")
				.build();
		mockMvc.perform(put("/auth/{userId}/password", "f8b63a43-e3dd-41ae-8253-e764aba1610c").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(changePasswordRequestDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value("f8b63a43-e3dd-41ae-8253-e764aba1610c"))
				.andExpect(jsonPath("$.data.userName").value("abcsdfffemmggmmllhhlllkkk@gmail.com"))
				.andExpect(jsonPath("$.data.name").value("ABC"))
				.andExpect(jsonPath("$.data.surname").value("ABC"))
				.andExpect(jsonPath("$.data.role").value("USER"))
				.andExpect(jsonPath("$.message").value("Password was changed successfully"));
	}

	@Test
	public void givenSendMailURI_whenMockMVC_thenVerifyResponse() throws Exception {
		ForgotPasswordRequestDto forgotPasswordRequestDto = ForgotPasswordRequestDto.builder()
				.email("tuanql90@gmail.com")
				.build();
		mockMvc.perform(post("/auth/sendMail").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(forgotPasswordRequestDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Email sent... please verify account within 1 minute"));
	}
}
