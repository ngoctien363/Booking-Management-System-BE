package com.tip.dg4.dc4.bookingmanagementsystem.shared.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;

/**
 * OtpUtil
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/12/2023
 * @since 1.0.0
 */
@Component
public class OtpUtil {
	public String generateOTP() {
		Random random = new Random();
		int randomNumber = random.nextInt(AppConstant.RANDOM);
		StringBuilder output = new StringBuilder(Integer.toString(randomNumber));

		while (output.length() < AppConstant.OTP_LENGTH) {
			output.insert(0, AppConstant.ZERO);
		}
		return output.toString();
	}
}
