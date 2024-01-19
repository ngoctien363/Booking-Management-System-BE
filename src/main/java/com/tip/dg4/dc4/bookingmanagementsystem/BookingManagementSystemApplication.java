package com.tip.dg4.dc4.bookingmanagementsystem;

import com.tip.dg4.dc4.bookingmanagementsystem.models.User;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.UserRole;
import com.tip.dg4.dc4.bookingmanagementsystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class BookingManagementSystemApplication implements CommandLineRunner {
	private final UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(BookingManagementSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(!userService.isExistingRole(UserRole.ADMIN)){
			User user = User.builder()
					.email("admin@gmail.com")
					.name("Admin")
					.surname("Admin")
					.password(new BCryptPasswordEncoder().encode("Admin"))
					.role(UserRole.ADMIN)
					.phone("123")
					.isActive(true)
					.build();
			userService.save(user);
		}
	}
}
