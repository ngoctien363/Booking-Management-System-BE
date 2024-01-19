package com.tip.dg4.dc4.bookingmanagementsystem.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * This class is used to create the response DTO object for User object
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/12/2023
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
	private UUID id;

	private String email;

	@JsonIgnore
	private String password;

	private String surname;

	private String name;

	private String phone;

	private UserRole role;

	@JsonProperty(value = "isActive")
	private boolean isActive;
}
