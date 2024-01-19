package com.tip.dg4.dc4.bookingmanagementsystem.services.implement;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.BookingDetailDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.booking.GetAllBookingHistoryDto;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.NotImplementException;
import com.tip.dg4.dc4.bookingmanagementsystem.services.BookingDetailService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.EmailService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * This is the implementation of the Email service interface
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 12/11/2023
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	private final JavaMailSender javaMailSender;
	private final TemplateEngine templateEngine;
	private final BookingDetailService bookingDetailService;

	@Value("${spring.mail.username}")
	private String mailUsername;

	@Override
	public void sendEmail(String to, String otp) throws MessagingException{
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(AppConstant.MAIL_SUBJECT);
			mimeMessageHelper.setText(AppConstant.MAIL_BODY + otp);
			javaMailSender.send(mimeMessage);
	}

	@Override
	public void confirmBookingRoom(GetAllBookingHistoryDto bookingHistoryDto) {
		try {
			List<BookingDetailDto> bookingDetails = bookingDetailService.getByBookingHistoryId(bookingHistoryDto.getId());
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			Context context = new Context();
			context.setVariable("customerName", bookingHistoryDto.getCustomer());
			context.setVariable("hotelName", bookingHistoryDto.getHotelName());
			context.setVariable("checkInDate", bookingHistoryDto.getCheckInDate().format(DateTimeFormatter.ofPattern(AppConstant.DATE_FORMAT)));
			context.setVariable("checkOutDate", bookingHistoryDto.getCheckOutDate().format(DateTimeFormatter.ofPattern(AppConstant.DATE_FORMAT)));
			context.setVariable("duration", bookingHistoryDto.getDuration());
			context.setVariable("detail", bookingDetails);
			context.setVariable("totalPrice", bookingHistoryDto.getTotalPrice());
			String htmlContext = templateEngine.process("successfulBooking", context);

			helper.setFrom(mailUsername, "BMS-DC4");
			helper.setTo(bookingHistoryDto.getEmail());
			helper.setSubject("[BMS-DC4] Đặt phòng thành công");
			helper.setText(htmlContext, true);

			javaMailSender.send(message);
		} catch (Exception e) {
            throw new NotImplementException("Something went wrong");
		}
	}
}
