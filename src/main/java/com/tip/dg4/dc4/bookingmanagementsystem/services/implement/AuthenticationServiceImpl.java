package com.tip.dg4.dc4.bookingmanagementsystem.services.implement;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.ChangePasswordRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.JWTAuthenticationResponseDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.OtpResponseDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.RefreshTokenRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.ResetPasswordRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.SignInRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.SignUpRequestDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.UserResponseDto;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.BadRequestException;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.NotFoundException;
import com.tip.dg4.dc4.bookingmanagementsystem.mappers.UserMapper;
import com.tip.dg4.dc4.bookingmanagementsystem.models.User;
import com.tip.dg4.dc4.bookingmanagementsystem.models.enums.UserRole;
import com.tip.dg4.dc4.bookingmanagementsystem.services.AuthenticationService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.EmailService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.JWTService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.UserService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.ExceptionConstant;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

/**
 * AuthenticationServiceImpl
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/30/2023
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final UserMapper userMapper;
	private final AuthenticationManager authenticationManager;
	private final JWTService jwtService;
	private final OtpUtil otpUtil;
	private final EmailService emailService;
	private final HandlerExceptionResolver handlerExceptionResolver;
	private OtpResponseDto otpResponseDto;
	@Override
	public UserResponseDto signUp(SignUpRequestDto signUpRequestDto) {
		if (!userService.isExistingEmail(signUpRequestDto.getEmail())){
			User user = modelMapper.map(signUpRequestDto, User.class);
			user.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
			user.setRole(UserRole.USER);
			return userService.save(user);
		} else {
			throw new BadRequestException(ExceptionConstant.USER_E004);
		}
	}

	@Override
	public JWTAuthenticationResponseDto signIn(SignInRequestDto signInRequestDto) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequestDto.getEmail(),
				signInRequestDto.getPassword()));
		User user = userService.retrieveByEmail(signInRequestDto.getEmail());
		return createAuthenticationObject(user);
	}

	@Override
	public JWTAuthenticationResponseDto changePassword(UUID id, ChangePasswordRequestDto changePasswordRequestDto) {
		User user = modelMapper.map(userService.retrieveById(id), User.class);
		if(passwordEncoder.matches(changePasswordRequestDto.getOldPassword(), user.getPassword())){
			user.setPassword(passwordEncoder.encode(changePasswordRequestDto.getNewPassword()));
			userService.save(user);
			return createAuthenticationObject(user);
		} else {
			throw new NotFoundException(ExceptionConstant.USER_E003);
		}
	}

	@Override
	public UserResponseDto resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
		if(resetPasswordRequestDto.getOtp().equals(otpResponseDto.getOtp())){
			if(Duration.between(otpResponseDto.getOtpGeneratedTime(),
					LocalDateTime.now()).getSeconds() < AppConstant.OTP_EXPIRATION_TIME){
				User user = userService.retrieveByEmail(resetPasswordRequestDto.getEmail());
				user.setPassword(passwordEncoder.encode(resetPasswordRequestDto.getNewPassword()));
				return userService.save(user);
			} else {
				throw new BadRequestException(ExceptionConstant.USER_E008);
			}
		} else {
			throw new NotFoundException(ExceptionConstant.USER_E006);
		}
	}

	@Override
	public JWTAuthenticationResponseDto refresh(RefreshTokenRequestDto refreshTokenRequestDto) {
		String userEmail = jwtService.extractUserName(refreshTokenRequestDto.getToken());
		User user = userService.retrieveByEmail(userEmail);
		if(jwtService.isTokenValid(refreshTokenRequestDto.getToken(), user)){
			String jwt = jwtService.generateToken(user);
			JWTAuthenticationResponseDto jwtAuthenticationResponseDto = userMapper.convertUserToJWTAuthenticationResponse(user);
			jwtAuthenticationResponseDto.setToken(jwt);
			jwtAuthenticationResponseDto.setRefreshToken(refreshTokenRequestDto.getToken());
			return jwtAuthenticationResponseDto;
		}
		return null;
	}

	@Override
	public JWTAuthenticationResponseDto createAuthenticationObject(User user) {
		String jwt = jwtService.generateToken(user);
		String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
		JWTAuthenticationResponseDto jwtAuthenticationResponseDto = userMapper.convertUserToJWTAuthenticationResponse(user);
		jwtAuthenticationResponseDto.setToken(jwt);
		jwtAuthenticationResponseDto.setRefreshToken(refreshToken);
		return jwtAuthenticationResponseDto;
	}

	@Override
	public void generateOtp(String email) throws MessagingException {
		if(userService.isExistingEmail(email)){
			String otp = otpUtil.generateOTP();
			otpResponseDto = new OtpResponseDto(otp, LocalDateTime.now());
			emailService.sendEmail(email, otp);
		} else {
			throw new NotFoundException(ExceptionConstant.USER_E005);
		}
	}
}
