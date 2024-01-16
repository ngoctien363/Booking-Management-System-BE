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

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByEmail(String email);
	User findByRole(UserRole role);
	boolean existsByEmail(String email);
	@Query(value = "SELECT * FROM users m WHERE m.name LIKE %?1% OR m.surname LIKE %?1% OR m.email LIKE %?1%", nativeQuery = true)
	Page<User> findAllUserByNameOrSurnameOrEmail(String keyword, Pageable pageable);
}
