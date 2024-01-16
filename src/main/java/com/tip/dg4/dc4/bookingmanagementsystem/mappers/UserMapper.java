package com.tip.dg4.dc4.bookingmanagementsystem.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.JWTAuthenticationResponseDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.UserResponseDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.UserUpdateRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

/**
 * UserMapper
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/12/2023
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class UserMapper {
	private final ModelMapper modelMapper;
	public JWTAuthenticationResponseDto convertUserToJWTAuthenticationResponse(User user){
		JWTAuthenticationResponseDto jwtAuthenticationResponseDto = modelMapper.map(user, JWTAuthenticationResponseDto.class);
		jwtAuthenticationResponseDto.setUserName(user.getEmail());
		return jwtAuthenticationResponseDto;
	}
	public Page<UserResponseDto> convertPageUserToPageUserResponse(Page<User> users){
		List<UserResponseDto> userResponses = new ArrayList<>();
		users.forEach(user ->
				userResponses.add(modelMapper.map(user, UserResponseDto.class))
		);
		return new PageImpl<>(userResponses);
	}
	public UserResponseDto convertUserToUserResponse(User user){
		return modelMapper.map(user, UserResponseDto.class);
	}
}
