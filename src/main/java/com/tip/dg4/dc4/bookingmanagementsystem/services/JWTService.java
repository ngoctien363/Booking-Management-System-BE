package com.tip.dg4.dc4.bookingmanagementsystem.services;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * JWTService
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/29/2023
 * @since 1.0.0
 */
public interface JWTService {
	/**
	 * This method is used for extract the name of the User
	 * @param token The token of this User
	 * @return
	 */
	String extractUserName(String token);

	/**
	 * This method is used to generate the token for the User
	 * @param userDetails The object that contains the user info needing to generate token
	 * @return The token
	 */
	String generateToken(UserDetails userDetails);

	String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);
	boolean isTokenValid(String token, UserDetails userDetails);

	boolean isTokenExpired(String token);
}
