package com.tip.dg4.dc4.bookingmanagementsystem.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.UserUpdateDto;
import com.tip.dg4.dc4.bookingmanagementsystem.services.UserService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.res.DataResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * The controller for the User role
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/30/2023
 * @since 1.0.0
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {
	private final UserService userService;
	private final HttpStatus httpStatus= HttpStatus.OK;

	DataResponse dataResponse;

	@PutMapping("/{id}")
	public ResponseEntity<DataResponse> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
		 dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"User was updated successfully!",
				userService.update(id, userUpdateDto)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}
	@GetMapping("/{id}")
	public ResponseEntity<DataResponse> getUserById(@PathVariable UUID id){
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"User was got successfully!",
				userService.retrieveById(id)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}
}