package com.tip.dg4.dc4.bookingmanagementsystem.services;

import java.util.UUID;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.ChangePasswordDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.JWTAuthenticationDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.RefreshTokenDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.ResetPasswordDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.SignInDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.SignUpDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.UserDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.User;
import jakarta.mail.MessagingException;

/**
 * This is the service interface for the authentication
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/30/2023
 * @since 1.0.0
 */
public interface AuthenticationService {
	/**
	 * This function is used for User to sign up
	 * @param signUpDto is the object contains the info that User input to sign up
	 * @return a UserResponseDto object contains the info of User
	 */
	UserDto signUp(SignUpDto signUpDto);

	/**
	 * This function is used for User to sign in
	 * @param signInDto is the object contains the info that User input to sign in
	 * @return a JWTAuthenticationResponseDto object contains the info and the token of User
	 */
	JWTAuthenticationDto signIn(SignInDto signInDto);

	/**
	 * This function is used for User to change the password
	 * @param id is the id of User
	 * @param changePasswordDto is an object contains the current password of the User and the new password User that the User wants to change
	 * @return a JWTAuthenticationResponseDto object contains the info and the token of User
	 */
	JWTAuthenticationDto changePassword(UUID id, ChangePasswordDto changePasswordDto);

	/**
	 * This function is used for the User to reset password
	 * @param resetPasswordDto is an object contains the User's email, the otp used to verify and the new password that the User wants to change
	 * @return a UserResponseDto object
	 */
	UserDto resetPassword(ResetPasswordDto resetPasswordDto);

	/**
	 * This function is used to refresh the token
	 * @param refreshTokenDto is an object contains the refresh token used to refresh
	 * @return a JWTAuthenticationResponseDto object contains the info and the token of User
	 */
	JWTAuthenticationDto refresh(RefreshTokenDto refreshTokenDto);

	/**
	 * This function is used to create an Authentication object for a User, that contains the token and refresh token for a User
	 * @param user is the User object needing to create the authentication
	 * @return a JWTAuthenticationResponseDto object contains the info and the token of User
	 */
	JWTAuthenticationDto createAuthenticationObject(User user);

	/**
	 * This function is used to create the Otp
	 * @param email is the email of the User needing to generate OTP
	 * @throws MessagingException is the exception will be thrown if the email cannot be sent
	 */
	void generateOtp(String email) throws MessagingException;
}
