package com.tip.dg4.dc4.bookingmanagementsystem.dto;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserUpdateRequest
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/13/2023
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserUpdateRequestDto {
		@Nullable
		private String surname;
	  @Nullable
		private String name;
		@Nullable
		@Pattern(regexp = "^\\d{10}$", message = "Phone number has 10 numbers!")
		private String phone;
		private boolean isActive;
}
