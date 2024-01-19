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
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.ChangePasswordDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.ForgotPasswordDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.SignInDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.SignUpDto;
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
	public void signUp_whenValid_thenResponseOk() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.name("ABC")
				.surname("ABC")
				.password("ABC")
				.phone("1233211233")
				.isActive(true)
				.build();
		mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.email").value("abcsdfffemmggmmllhhlllkkk@gmail.com"))
				.andExpect(jsonPath("$.data.name").value("ABC"))
				.andExpect(jsonPath("$.data.surname").value("ABC"))
				.andExpect(jsonPath("$.data.phone").value("1233211233"))
				.andExpect(jsonPath("$.data.role").value("USER"))
				.andExpect(jsonPath("$.data.active").value(true))
				.andExpect(jsonPath("$.message").value("User was created successfully!"));
	}

	@Test
	public void signUp_whenInvalidEmail_thenResponseBadRequest() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.email("abcsdfffemmggmmllhhlllkk")
				.name("ABC")
				.surname("ABC")
				.password("ABC")
				.phone("1233211233")
				.isActive(true)
				.build();
		mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Email is not valid! Example: username@domain.com"));
	}

	@Test
	public void signUp_whenNullEmail_thenResponseBadRequest() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.name("ABC")
				.surname("ABC")
				.password("ABC")
				.phone("1233211233")
				.isActive(true)
				.build();
		mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Email can not be null!"));
	}

	@Test
	public void signUp_whenNullName_thenResponseBadRequest() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.surname("ABC")
				.password("ABC")
				.phone("1233211233")
				.isActive(true)
				.build();
		mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Name can not be null!"));
	}

	@Test
	public void signUp_whenNullSurname_thenResponseBadRequest() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.name("ABC")
				.password("ABC")
				.phone("1233211233")
				.isActive(true)
				.build();
		mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Surname can not be null!"));
	}

	@Test
	public void signUp_whenNullPassword_thenResponseBadRequest() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.name("ABC")
				.surname("ABC")
				.phone("1233211233")
				.isActive(true)
				.build();
		mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Password can not be null!"));
	}

	@Test
	public void signUp_whenNullPhone_thenResponseBadRequest() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.name("ABC")
				.surname("ABC")
				.password("ABC")
				.isActive(true)
				.build();
		mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Phone can not be null!"));
	}

	@Test
	public void signUp_whenInvalidPhone_thenResponseBadRequest() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.name("ABC")
				.surname("ABC")
				.password("ABC")
				.phone("12332")
				.isActive(true)
				.build();
		mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Phone number has 10 numbers!"));
	}

	@Test
	public void signIn_whenValid_thenResponseOk() throws Exception {
		SignInDto signInDto = SignInDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.password("ABC")
				.build();
		mockMvc.perform(post("/auth/signin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signInDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.userName").value("abcsdfffemmggmmllhhlllkkk@gmail.com"))
				.andExpect(jsonPath("$.data.name").value("ABC"))
				.andExpect(jsonPath("$.data.surname").value("ABC"))
				.andExpect(jsonPath("$.data.role").value("USER"))
				.andExpect(jsonPath("$.message").value("Sign in successfully!"));
	}

	@Test
	public void signIn_whenNullEmail_thenResponseBadRequest() throws Exception {
		SignInDto signInDto = SignInDto.builder()
				.password("ABC")
				.build();
		mockMvc.perform(post("/auth/signin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signInDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Email can not be null!"));
	}

	@Test
	public void signIn_whenInvalidEmail_thenResponseBadRequest() throws Exception {
		SignInDto signInDto = SignInDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk")
				.password("ABC")
				.build();
		mockMvc.perform(post("/auth/signin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signInDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Email is not valid! Example: username@domain.com"));
	}

	@Test
	public void signIn_whenWrongEmail_thenResponseBadRequest() throws Exception {
		SignInDto signInDto = SignInDto.builder()
				.email("abcsdfllkkk@gmail.com")
				.password("ABC")
				.build();
		mockMvc.perform(post("/auth/signin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signInDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Wrong user name or password!"));
	}

	@Test
	public void signIn_whenNullPassword_thenResponseBadRequest() throws Exception {
		SignInDto signInDto = SignInDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.build();
		mockMvc.perform(post("/auth/signin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signInDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Password can not be null!"));
	}

	@Test
	public void signIn_whenWrongPassword_thenResponseUnauthorized() throws Exception {
		SignInDto signInDto = SignInDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.password("ABCXYZ")
				.build();
		mockMvc.perform(post("/auth/signin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signInDto)))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Wrong user name or password!"));
	}

	@Test
	public void changePassword_whenValid_thenResponseOk() throws Exception {
		ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
				.oldPassword("ABC")
				.newPassword("ABC123")
				.build();
		mockMvc.perform(put("/auth/{userId}/password", "f8b63a43-e3dd-41ae-8253-e764aba1610c").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(changePasswordDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value("f8b63a43-e3dd-41ae-8253-e764aba1610c"))
				.andExpect(jsonPath("$.data.userName").value("abcsdfffemmggmmllhhlllkkk@gmail.com"))
				.andExpect(jsonPath("$.data.name").value("ABC"))
				.andExpect(jsonPath("$.data.surname").value("ABC"))
				.andExpect(jsonPath("$.data.role").value("USER"))
				.andExpect(jsonPath("$.message").value("Password was changed successfully!"));
	}

	@Test
	public void changePassword_whenWrongPassword_thenResponseUnauthorized() throws Exception {
		ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
				.oldPassword("ABCDD")
				.newPassword("ABC123")
				.build();
		mockMvc.perform(put("/auth/{userId}/password", "f8b63a43-e3dd-41ae-8253-e764aba1610c").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(changePasswordDto)))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Wrong password!"));
	}

	@Test
	public void changePassword_whenNullOldPassword_thenResponseBadRequest() throws Exception {
		ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
				.newPassword("ABC123")
				.build();
		mockMvc.perform(put("/auth/{userId}/password", "f8b63a43-e3dd-41ae-8253-e764aba1610c").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(changePasswordDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Password can not be null!"));
	}

	@Test
	public void changePassword_whenNullNewPassword_thenResponseBadRequest() throws Exception {
		ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
				.oldPassword("ABC")
				.build();
		mockMvc.perform(put("/auth/{userId}/password", "f8b63a43-e3dd-41ae-8253-e764aba1610c").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(changePasswordDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Password can not be null!"));
	}

	@Test
	public void sendMail_whenValid_thenResponseOk() throws Exception {
		ForgotPasswordDto forgotPasswordDto = ForgotPasswordDto.builder()
				.email("tuanql90@gmail.com")
				.build();
		mockMvc.perform(post("/auth/sendMail").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(forgotPasswordDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Email sent... please verify account within 1 minute"));
	}

	@Test
	public void sendMail_whenInvalidEmail_thenResponseNotFound() throws Exception {
		ForgotPasswordDto forgotPasswordDto = ForgotPasswordDto.builder()
				.email("tuanq444l90@gmail.com")
				.build();
		mockMvc.perform(post("/auth/sendMail").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(forgotPasswordDto)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Wrong email!"));
	}
}
