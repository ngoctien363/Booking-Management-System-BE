package com.tip.dg4.dc4.bookingmanagementsystem.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.JWTAuthenticationDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.UserDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

/**
 * This is the mapping class for User object
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/12/2023
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class UserMapper {
	private final ModelMapper modelMapper;
	public JWTAuthenticationDto convertUserToJWTAuthenticationResponse(User user){
		JWTAuthenticationDto jwtAuthenticationDto = modelMapper.map(user, JWTAuthenticationDto.class);
		jwtAuthenticationDto.setUserName(user.getEmail());
		return jwtAuthenticationDto;
	}
	public Page<UserDto> convertPageUserToPageUserResponse(Page<User> users){
		List<UserDto> userResponses = new ArrayList<>();
		users.forEach(user ->
				userResponses.add(modelMapper.map(user, UserDto.class))
		);
		return new PageImpl<>(userResponses);
	}
	public UserDto convertUserToUserResponse(User user){
		return modelMapper.map(user, UserDto.class);
	}
}
