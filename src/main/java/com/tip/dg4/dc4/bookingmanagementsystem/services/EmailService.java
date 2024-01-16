package com.tip.dg4.dc4.bookingmanagementsystem.services;


import jakarta.mail.MessagingException;

/**
 * EmailService
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/11/2023
 * @since 1.0.0
 */
public interface EmailService {
	void sendEmail(String to, String otp) throws MessagingException;

}
