package com.tip.dg4.dc4.bookingmanagementsystem.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OtpResponse
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/12/2023
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OtpResponseDto {
	private String otp;
	private LocalDateTime otpGeneratedTime;
}
