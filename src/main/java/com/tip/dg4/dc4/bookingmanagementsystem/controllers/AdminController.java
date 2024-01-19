package com.tip.dg4.dc4.bookingmanagementsystem.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.SignUpDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.UserDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.UserUpdateDto;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.BadRequestException;
import com.tip.dg4.dc4.bookingmanagementsystem.services.AuthenticationService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.UserService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.ExceptionConstant;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.res.DataResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * The controller for the admin role
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/30/2023
 * @since 1.0.0
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {
	private final UserService userService;
	private final AuthenticationService authenticationService;
	private final HttpStatus httpStatus= HttpStatus.OK;
	private DataResponse dataResponse;

	@GetMapping("/{id}")
	public ResponseEntity<DataResponse> getUserById(@PathVariable UUID id){
		 dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"User was got successfully!",
				userService.retrieveById(id)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}

	@GetMapping()
	public ResponseEntity<DataResponse> searchUser(@RequestParam(value = "keyword", required = false) String searchingValue,
			@RequestParam(defaultValue = AppConstant.PAGE_INDEX_DEFAULT) int page,
			@RequestParam(defaultValue = AppConstant.PAGE_SIZE_DEFAULT) int size){
		Page<UserDto> userResponses;
		try {
			Pageable pageable = PageRequest.of(page-1, size, Sort.by("name").and(Sort.by("surname")));
			if(null==searchingValue){
				userResponses = userService.retrieveAll(pageable);
			} else {
				userResponses = userService.searchUser(searchingValue ,pageable);
			}
			dataResponse = new DataResponse(
					httpStatus.getReasonPhrase(),
					"Users was got successfully!",
					userResponses.getContent()
			);
			return new ResponseEntity<>(dataResponse, httpStatus);
		} catch (IllegalArgumentException e){
			throw new BadRequestException(ExceptionConstant.USER_E011);
		}
	}

	@PostMapping()
	public ResponseEntity<DataResponse> createUser(@Valid @RequestBody SignUpDto signUpDto){
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"User was created successfully!",
				authenticationService.signUp(signUpDto)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}

	@PutMapping("/{id}")
	public ResponseEntity<DataResponse> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateDto userUpdateDto){
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"User was updated successfully!",
				userService.update(id, userUpdateDto)
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<DataResponse> deleteUser(@PathVariable UUID id){
		userService.deleteById(id);
		dataResponse = new DataResponse(
				httpStatus.getReasonPhrase(),
				"User was deleted successfully!"
		);
		return new ResponseEntity<>(dataResponse, httpStatus);
	}
}
