package com.tip.dg4.dc4.bookingmanagementsystem.services;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.UserResponseDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.UserUpdateRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.User;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.UUID;

/**
 * UserService
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/29/2023
 * @since 1.0.0
 */
public interface UserService {
	UserDetailsService userDetailsService();
	Page<UserResponseDto> searchUser(String keyword, Pageable pageable);
	User retrieveByRole(UserRole role);
	User retrieveByEmail(String email);
	UserResponseDto retrieveById(UUID id);
	void deleteById(UUID id);
	Page<UserResponseDto> retrieveAll(Pageable pageable);
	UserResponseDto save(User user);
	UserResponseDto update(UUID id, UserUpdateRequestDto userUpdateRequestDto);
	boolean isExistingEmail(String email);
	Optional<User> findById(UUID id);
}
