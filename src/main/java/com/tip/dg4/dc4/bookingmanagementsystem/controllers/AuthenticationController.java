package com.tip.dg4.dc4.bookingmanagementsystem.controllers;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.RefreshTokenDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.SignInDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.SignUpDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.ChangePasswordDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.ForgotPasswordDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.ResetPasswordDto;
import com.tip.dg4.dc4.bookingmanagementsystem.services.AuthenticationService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.res.DataResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * The controller for the authentication
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/30/2023
 * @since 1.0.0
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {
	private final AuthenticationService authenticationService;
	private final HttpStatus httpStatus= HttpStatus.OK;
	DataResponse dataResponse;

	@PostMapping("/signup")
	public ResponseEntity<DataResponse> signUp(@Valid @RequestBody SignUpDto signUpDto){
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"User was created successfully!",
				authenticationService.signUp(signUpDto)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}

	@PostMapping("/signin")
	public ResponseEntity<DataResponse> signIn(@Valid @RequestBody SignInDto signInDto){
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"Sign in successfully!",
				authenticationService.signIn(signInDto)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}

	@PostMapping("/refresh")
	public ResponseEntity<DataResponse> refresh(@RequestBody RefreshTokenDto refreshTokenDto){
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"Token was refreshed successfully!",
				authenticationService.refresh(refreshTokenDto)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}

	@PutMapping("/{userId}/password")
	public ResponseEntity<DataResponse> changePassword(@PathVariable UUID userId,@Valid @RequestBody ChangePasswordDto changePasswordDto){
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"Password was changed successfully!",
				authenticationService.changePassword(userId, changePasswordDto)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}

	@PostMapping("/sendMail")
	public ResponseEntity<DataResponse> sendMail(@RequestBody ForgotPasswordDto forgotPasswordDto) throws MessagingException {
		authenticationService.generateOtp(forgotPasswordDto.getEmail());
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				AppConstant.MAIL_NOTICE
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}

	@PutMapping("/verifyAccount")
	public ResponseEntity<DataResponse> verifyAccount(@RequestBody ResetPasswordDto resetPasswordDto){
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"Password was reset successfully!",
				authenticationService.resetPassword(resetPasswordDto)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}
}
