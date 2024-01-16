package com.tip.dg4.dc4.bookingmanagementsystem.controllers;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.*;
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
 * AuthenticationController
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
	public ResponseEntity<DataResponse> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto){
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"User was created successfully",
				authenticationService.signUp(signUpRequestDto)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}

	@PostMapping("/signin")
	public ResponseEntity<DataResponse> signIn(@RequestBody SignInRequestDto signInRequestDto){
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"Sign in successfully",
				authenticationService.signIn(signInRequestDto)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}

	@PostMapping("/refresh")
	public ResponseEntity<DataResponse> refresh(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto){
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"Token was refreshed successfully",
				authenticationService.refresh(refreshTokenRequestDto)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}

	@PutMapping("/{userId}/password")
	public ResponseEntity<DataResponse> changePassword(@PathVariable UUID userId, @RequestBody ChangePasswordRequestDto changePasswordRequestDto){
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"Password was changed successfully",
				authenticationService.changePassword(userId, changePasswordRequestDto)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}

	@PostMapping("/sendMail")
	public ResponseEntity<DataResponse> sendMail(@RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) throws MessagingException {
		authenticationService.generateOtp(forgotPasswordRequestDto.getEmail());
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				AppConstant.MAIL_NOTICE
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}

	@PutMapping("/verifyAccount")
	public ResponseEntity<DataResponse> verifyAccount(@RequestBody ResetPasswordRequestDto resetPasswordRequestDto){
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"Password was reset successfully",
				authenticationService.resetPassword(resetPasswordRequestDto)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}
}
