package com.tip.dg4.dc4.bookingmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ChangePasswordRequest
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/7/2023
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChangePasswordRequestDto {
	private String oldPassword;
	private String newPassword;
}
