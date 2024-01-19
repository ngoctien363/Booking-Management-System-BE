package com.tip.dg4.dc4.bookingmanagementsystem.services;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.UserDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.UserUpdateDto;
import com.tip.dg4.dc4.bookingmanagementsystem.models.User;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.UUID;

/**
 * This is the service interface of User model that defines abstraction methods of User
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/29/2023
 * @since 1.0.0
 */
public interface UserService {
	/**
	 * This function is used to implement operations of UserDetail service
	 * @return function of UserDetail service
	 */
	UserDetailsService userDetailsService();

	/**
	 * This function is used to search Users
	 * @param keyword is the name or the surname or the email of Users needing to be searched
	 * @param pageable an object of Pageable interface used to paging
	 * @return a page of UserResponseDto objects
	 */
	Page<UserDto> searchUser(String keyword, Pageable pageable);

	/**
	 * This function is used to check the existence of the User objects that have the corresponding role
	 * @param role is role of Users needing to be checked
	 * @return true if the role is existing
	 */
	boolean isExistingRole(UserRole role);

	/**
	 * This function is used to retrieve the User object by the corresponding email
	 * @param email is the email of the User needing to be retrieved
	 * @return a User object
	 */
	User retrieveByEmail(String email);

	/**
	 * This function is used to retrieve the User object by corresponding ID
	 * @param id is the ID of the User needing to be retrieved
	 * @return a UserResponseDto object
	 */
	UserDto retrieveById(UUID id);

	/**
	 * This function is used to delete a User object by the corresponding ID
	 * @param id is the ID of the User needing to be deleted
	 */
	void deleteById(UUID id);

	/**
	 * This function is used to retrieve all User objects existing in the database
	 * @param pageable an object of Pageable interface used to paging
	 * @return a page of UserResponseDto objects
	 */
	Page<UserDto> retrieveAll(Pageable pageable);

	/**
	 * This function is used to save a User object to the database
	 * @param user the User object needing to be saved
	 * @return a UserResponseDto object
	 */
	UserDto save(User user);

	/**
	 * This function is used to edit the info of a User object that existing in the database
	 * @param id is the id of the User object needing to be edited
	 * @param userUpdateDto the object that contains the info need to be edited of the User object
	 * @return a UserResponseDto object
	 */
	UserDto update(UUID id, UserUpdateDto userUpdateDto);

	/**
	 * This function is used to check the existence of the User objects that have the corresponding email
	 * @param email is email of Users needing to be checked
	 * @return true if the email is existing
	 */
	boolean isExistingEmail(String email);

	Optional<User> findById(UUID id);
}
