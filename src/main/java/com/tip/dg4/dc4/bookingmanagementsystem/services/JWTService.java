package com.tip.dg4.dc4.bookingmanagementsystem.services;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * This is the service interface of JWT
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/29/2023
 * @since 1.0.0
 */
public interface JWTService {
	/**
	 * This function is used for extract the name of the User
	 * @param token The token of this User
	 * @return a token
	 */
	String extractUserName(String token);

	/**
	 * This function is used to generate the token for the User
	 * @param userDetails an object that contains the user info needing to be used with Spring Security
	 * @return a token has already been created
	 */
	String generateToken(UserDetails userDetails);

	/**
	 * This function is used to refresh the token
	 * @param extraClaims a map used to create the claims object
	 * @param userDetails an object that contains the user info needing to be used with Spring Security
	 * @return a token has already been created
	 */
	String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);

	/**
	 * This function is used to check the token
	 * @param token is the token needing to be checked
	 * @param userDetails an object that contains the user info needing to be used with Spring Security
	 * @return true if the token is valid
	 */
	boolean isTokenValid(String token, UserDetails userDetails);

	/**
	 * This function is used to check to expiration of the token
	 * @param token is the token needing to be checked
	 * @return true if the token is expired
	 */
	boolean isTokenExpired(String token);
}
