package com.tip.dg4.dc4.bookingmanagementsystem.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.SignUpDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.UserUpdateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * AdminControllerTest
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 1/4/2024
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AdminControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void searchUser_whenParamEmpty_thenResponseOk() throws Exception {
		mockMvc.perform(get("/admin").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.length()").value(10))
				.andExpect(jsonPath("$.message").value("Users was got successfully!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void searchUser_whenKeywordHasValue_thenResponseOk() throws Exception {
		mockMvc.perform(get("/admin").param("keyword", "abcsdfffemmggmmllhhlllkkk@gmail.com").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].name").value("TUAN"))
				.andExpect(jsonPath("$.data[0].surname").value("ABC"))
				.andExpect(jsonPath("$.data[0].email").value("abcsdfffemmggmmllhhlllkkk@gmail.com"))
				.andExpect(jsonPath("$.data[0].phone").value("1233211233"))
				.andExpect(jsonPath("$.data[0].role").value("USER"))
				.andExpect(jsonPath("$.data[0].active").value(true))
				.andExpect(jsonPath("$.message").value("Users was got successfully!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void searchUser_whenPageAndSizeHasValue_thenResponseOk() throws Exception {
		mockMvc.perform(get("/admin").param("page", "1").param("size", "5").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.length()").value(5))
				.andExpect(jsonPath("$.message").value("Users was got successfully!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getUserById_whenValidId_thenResponseOk() throws Exception {
		mockMvc.perform(get("/user/{id}", "6299ac61-a11e-471f-951b-2bff3669d492").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.email").value("tuanql90@gmail.com"))
				.andExpect(jsonPath("$.data.name").value("User"))
				.andExpect(jsonPath("$.data.surname").value("John"))
				.andExpect(jsonPath("$.data.phone").value("123"))
				.andExpect(jsonPath("$.data.role").value("USER"))
				.andExpect(jsonPath("$.data.active").value(true))
				.andExpect(jsonPath("$.message").value("User was got successfully!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getUserById_whenInvalidId_thenResponseNotFound() throws Exception {
		mockMvc.perform(get("/admin/{id}", "6399ac61-a11e-471f-951b-2bff3669d492").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("This user does not exist!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void createUser_whenValid_thenResponseOk() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.name("ABC")
				.surname("ABC")
				.password("ABC")
				.phone("1233211233")
				.isActive(true)
				.build();
		mockMvc.perform(post("/admin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
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
	@WithMockUser(authorities = "ADMIN")
	public void createUser_whenInvalidEmail_thenResponseBadRequest() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.email("abcsdfffemmggmmllhhlllkk")
				.name("ABC")
				.surname("ABC")
				.password("ABC")
				.phone("1233211233")
				.isActive(true)
				.build();
		mockMvc.perform(post("/admin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Email is not valid! Example: username@domain.com"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void createUser_whenNullEmail_thenResponseBadRequest() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.name("ABC")
				.surname("ABC")
				.password("ABC")
				.phone("1233211233")
				.isActive(true)
				.build();
		mockMvc.perform(post("/admin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Email can not be null!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void createUser_whenNullName_thenResponseBadRequest() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.surname("ABC")
				.password("ABC")
				.phone("1233211233")
				.isActive(true)
				.build();
		mockMvc.perform(post("/admin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Name can not be null!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void createUser_whenNullSurname_thenResponseBadRequest() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.name("ABC")
				.password("ABC")
				.phone("1233211233")
				.isActive(true)
				.build();
		mockMvc.perform(post("/admin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Surname can not be null!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void createUser_whenNullPassword_thenResponseBadRequest() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.name("ABC")
				.surname("ABC")
				.phone("1233211233")
				.isActive(true)
				.build();
		mockMvc.perform(post("/admin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Password can not be null!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void createUser_whenNullPhone_thenResponseBadRequest() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.name("ABC")
				.surname("ABC")
				.password("ABC")
				.isActive(true)
				.build();
		mockMvc.perform(post("/admin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Phone can not be null!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void createUser_whenInvalidPhone_thenResponseBadRequest() throws Exception {
		SignUpDto signUpDto = SignUpDto.builder()
				.email("abcsdfffemmggmmllhhlllkkk@gmail.com")
				.name("ABC")
				.surname("ABC")
				.password("ABC")
				.phone("12332")
				.isActive(true)
				.build();
		mockMvc.perform(post("/admin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(signUpDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Phone number has 10 numbers!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void updateUser_whenValid_thenResponseOk() throws Exception {
		UserUpdateDto userUpdateDto = UserUpdateDto.builder()
				.name("TUAN")
				.build();
		mockMvc.perform(put("/admin/{id}","f8b63a43-e3dd-41ae-8253-e764aba1610c").content(new ObjectMapper().writeValueAsString(userUpdateDto)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.name").value("TUAN"))
				.andExpect(jsonPath("$.message").value("User was updated successfully!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void updateUser_whenInvalidId_thenResponseNotFound() throws Exception {
		UserUpdateDto userUpdateDto = UserUpdateDto.builder()
				.phone("1236666666")
				.build();
		mockMvc.perform(put("/admin/{id}", "8ccb3cc4-1174-41dd-96cf-97a2e06ba6fe").content(new ObjectMapper().writeValueAsString(userUpdateDto)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("This user does not exist!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void updateUser_whenInvalidPhoneNumber_thenResponseBadRequest() throws Exception {
		UserUpdateDto userUpdateDto = UserUpdateDto.builder()
				.phone("123")
				.build();
		mockMvc.perform(put("/admin/{id}","f8b63a43-e3dd-41ae-8253-e764aba1610c").content(new ObjectMapper().writeValueAsString(userUpdateDto)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Phone number has 10 numbers!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void deleteUser_whenValid_thenResponseOk() throws Exception {
		mockMvc.perform(delete("/admin/{id}","b1130e30-ded5-4ae3-af14-67fbfe255186").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("User was deleted successfully!"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void deleteUser_whenInvalidId_thenResponseNotFound() throws Exception {
		mockMvc.perform(delete("/admin/{id}", "8ccb3cc4-1174-41dd-96cf-97a2e06ba6fe").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("This user does not exist!"));
	}
}