package com.tip.dg4.dc4.bookingmanagementsystem.services;

import java.util.UUID;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.ChangePasswordRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.JWTAuthenticationResponseDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.RefreshTokenRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.ResetPasswordRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.SignInRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.SignUpRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.UserResponseDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.User;
import jakarta.mail.MessagingException;

/**
 * AuthenticationService
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/30/2023
 * @since 1.0.0
 */
public interface AuthenticationService {
	UserResponseDto signUp(SignUpRequestDto signUpRequestDto);
	JWTAuthenticationResponseDto signIn(SignInRequestDto signInRequestDto);
	JWTAuthenticationResponseDto changePassword(UUID id, ChangePasswordRequestDto changePasswordRequestDto);
	UserResponseDto resetPassword(ResetPasswordRequestDto resetPasswordRequestDto);
	JWTAuthenticationResponseDto refresh(RefreshTokenRequestDto refreshTokenRequestDto);
	JWTAuthenticationResponseDto createAuthenticationObject(User user);
	void generateOtp(String email) throws MessagingException;
}
