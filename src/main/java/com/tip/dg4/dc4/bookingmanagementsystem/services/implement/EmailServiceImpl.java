package com.tip.dg4.dc4.bookingmanagementsystem.services.implement;

import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.tip.dg4.dc4.bookingmanagementsystem.services.EmailService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;

/**
 * EmailServiceImpl
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/11/2023
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	private final JavaMailSender javaMailSender;

	@Override
	public void sendEmail(String to, String otp) throws MessagingException{
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(AppConstant.MAIL_SUBJECT);
			mimeMessageHelper.setText(AppConstant.MAIL_BODY + otp);
			javaMailSender.send(mimeMessage);
	}
}
