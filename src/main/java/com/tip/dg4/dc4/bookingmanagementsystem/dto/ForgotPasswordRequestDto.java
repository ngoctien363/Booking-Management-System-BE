package com.tip.dg4.dc4.bookingmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ForgetPasswordRequest
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/6/2023
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ForgotPasswordRequestDto {
	private String email;
}
