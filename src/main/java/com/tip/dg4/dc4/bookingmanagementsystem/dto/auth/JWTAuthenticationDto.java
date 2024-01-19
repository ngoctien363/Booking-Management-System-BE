package com.tip.dg4.dc4.bookingmanagementsystem.dto.auth;

import java.util.UUID;

import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to create the response DTO object when the User register JWT successful,
 * it contains the token and the refresh token for the User
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/30/2023
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JWTAuthenticationDto {
	private String token;
	private String refreshToken;
	private UUID id;
	private String userName;
	private String surname;
	private String name;
	private UserRole role;
}
