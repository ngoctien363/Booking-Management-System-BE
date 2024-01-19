package com.tip.dg4.dc4.bookingmanagementsystem.services;


import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.GetAllBookingHistoryDto;
import jakarta.mail.MessagingException;

/**
 * This is the service interface of Email sending
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/11/2023
 * @since 1.0.0
 */
public interface EmailService {
	/**
	 * This function is used to send the otp to the email of the User
	 * @param to is the email of the User
	 * @param otp is the OTP used to verify
	 * @throws MessagingException is the exception will be thrown if the email cannot be sent
	 */
	void sendEmail(String to, String otp) throws MessagingException;

	void confirmBookingRoom(GetAllBookingHistoryDto bookingHistoryDto);

}
