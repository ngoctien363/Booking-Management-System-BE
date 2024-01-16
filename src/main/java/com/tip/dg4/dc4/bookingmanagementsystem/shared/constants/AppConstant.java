package com.tip.dg4.dc4.bookingmanagementsystem.shared.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConstant {
	public static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
	public static final String DATE_FORMAT = "dd-MM-yyyy";
	public static final long TOKEN_EXPIRATION_TIME = 1000 * 60 * 60;
	public static final int OTP_EXPIRATION_TIME = 60;
	public static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 3;
	public static final String SECRET = "6ED5E90C9A159834B4470022591CE7B56BA2DEB1EA41BFD03A326B4CCA907CA9";
	public static final String BEARER = "Bearer ";
	public static final String[] AUTH_WHITELIST = {
			"/auth/**",
			"/hotels",
			"/hotels/{id}",
			"/rooms",
			"/rooms/{id}",
			"/rooms/getAvailableRooms/{id}"
	};
	public static final String EMPTY = "";
	public static final int RANDOM = 123456;
	public static final int OTP_LENGTH = 6;
	public static final String ZERO = "0";
	public static final String MAIL_SUBJECT = "Verify OTP";
	public static final String MAIL_BODY = "Your OTP is: ";
	public static final String MAIL_NOTICE = "Email sent... please verify account within 1 minute";
	public static final String DELETE_SUCCESSFUL = "Delete successful!";
	public static final String PAGE_SIZE_DEFAULT = "10";
	public static final String PAGE_INDEX_DEFAULT = "1";
}
