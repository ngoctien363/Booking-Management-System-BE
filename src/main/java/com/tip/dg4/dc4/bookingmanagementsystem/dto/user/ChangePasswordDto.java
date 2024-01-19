package com.tip.dg4.dc4.bookingmanagementsystem.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to create the object that contains the info to change password for the User
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/7/2023
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChangePasswordDto {
	@NotNull(message = "Password can not be null!")
	private String oldPassword;
	@NotNull(message = "Password can not be null!")
	private String newPassword;
}
