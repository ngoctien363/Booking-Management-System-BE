package com.tip.dg4.dc4.bookingmanagementsystem.repositories;

import com.tip.dg4.dc4.bookingmanagementsystem.models.User;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.UserRole;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This is the data access layer for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	/**
	 * Retrieve a User object by User's email
	 * @param email of User
	 * @return an optional User
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Check if there are any Users existing by the role
	 * @param role of User
	 * @return true if there are Users
	 */
	boolean existsByRole(UserRole role);

	/**
	 * Check if there are any Users existing by the email
	 * @param email of User
	 * @return true if there are Users
	 */
	boolean existsByEmail(String email);

	/**
	 * Retrieve all User objects by the name or the surname or the email of Users
	 * @param keyword used to search
	 * @param pageable used for paging
	 * @return a page of User objects
	 */
	@Query(value = "SELECT * FROM users m WHERE m.name LIKE %?1% OR m.surname LIKE %?1% OR m.email LIKE %?1%", nativeQuery = true)
	Page<User> findAllUserByNameOrSurnameOrEmail(String keyword, Pageable pageable);
}
