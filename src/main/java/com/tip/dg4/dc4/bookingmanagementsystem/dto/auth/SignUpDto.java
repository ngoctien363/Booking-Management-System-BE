package com.tip.dg4.dc4.bookingmanagementsystem.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to create the object that contains the info to signup for the User
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/30/2023
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SignUpDto {
	@Email(message = "Email is not valid! Example: username@domain.com", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	@NotNull(message = "Email can not be null!")
	private String email;
	@NotNull(message = "Password can not be null!")
	private String password;
	@NotNull(message = "Surname can not be null!")
	private String surname;
	@NotNull(message = "Name can not be null!")
	private String name;
	@Pattern(regexp = "^\\d{10}$", message = "Phone number has 10 numbers!")
	@NotNull(message = "Phone can not be null!")
	private String phone;
	private boolean isActive;
}
