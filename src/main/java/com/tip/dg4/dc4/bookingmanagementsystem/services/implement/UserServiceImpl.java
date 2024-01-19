package com.tip.dg4.dc4.bookingmanagementsystem.services.implement;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.UserDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.UserUpdateDto;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.NotFoundException;
import com.tip.dg4.dc4.bookingmanagementsystem.mappers.UserMapper;
import com.tip.dg4.dc4.bookingmanagementsystem.models.User;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.UserRole;
import com.tip.dg4.dc4.bookingmanagementsystem.repositories.UserRepository;
import com.tip.dg4.dc4.bookingmanagementsystem.services.UserService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.ExceptionConstant;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This is the implementation of User service interface
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/29/2023
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final UserMapper userMapper;

	@Override
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return retrieveByEmail(username);
			}
		};
	}

	@Override
	public Page<UserDto> searchUser(String keyword, Pageable pageable) {
		return userMapper.convertPageUserToPageUserResponse(userRepository.findAllUserByNameOrSurnameOrEmail(keyword, pageable));
	}

	@Override
	public boolean isExistingRole(UserRole role) {
		return userRepository.existsByRole(role);
	}

	@Override
	public User retrieveByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ExceptionConstant.USER_E001));
	}

	@Override
	public UserDto retrieveById(UUID id) {
		User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionConstant.USER_E001));
		return userMapper.convertUserToUserResponse(user);
	}

	@Override
	public void deleteById(UUID id) {
		if (userRepository.existsById(id)){
			userRepository.deleteById(id);
		} else {
			throw new NotFoundException(ExceptionConstant.USER_E001);
		}
	}

	@Override
	public Page<UserDto> retrieveAll(Pageable pageable) {
		List<User> users = userRepository.findAll(pageable).stream()
				.filter(user -> user.getRole().equals(UserRole.USER)).toList();

		return userMapper.convertPageUserToPageUserResponse(new PageImpl<>(users));
	}

	@Override
	public UserDto save(User user) {
			User newUser = userRepository.save(user);
			return userMapper.convertUserToUserResponse(newUser);
	}

	@Override
	public UserDto update(UUID id, UserUpdateDto userUpdateDto) {
		User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionConstant.USER_E001));
		modelMapper.map(userUpdateDto, user);
		return userMapper.convertUserToUserResponse(userRepository.save(user));
	}

	@Override
	public boolean isExistingEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public Optional<User> findById(UUID id) {
		return userRepository.findById(id);
	}
}
