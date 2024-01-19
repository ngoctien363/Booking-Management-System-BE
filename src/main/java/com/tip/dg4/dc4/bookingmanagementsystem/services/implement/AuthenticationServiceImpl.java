package com.tip.dg4.dc4.bookingmanagementsystem.services.implement;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.ChangePasswordDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.JWTAuthenticationDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.OtpResponseDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.RefreshTokenDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.ResetPasswordDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.SignInDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.auth.SignUpDto;
import com.tip.dg4.dc4.bookingmanagementsystem.dto.user.UserDto;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.BadRequestException;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.NotFoundException;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.UnauthorizedException;
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
	private OtpResponseDto otpResponseDto;
	@Override
	public UserDto signUp(SignUpDto signUpDto) {
		if (!userService.isExistingEmail(signUpDto.getEmail())){
			User user = modelMapper.map(signUpDto, User.class);
			user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
			user.setRole(UserRole.USER);
			return userService.save(user);
		} else {
			throw new BadRequestException(ExceptionConstant.USER_E004);
		}
	}

	@Override
	public JWTAuthenticationDto signIn(SignInDto signInDto) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.getEmail(),
				signInDto.getPassword()));
		User user = userService.retrieveByEmail(signInDto.getEmail());
		return createAuthenticationObject(user);
	}

	@Override
	public JWTAuthenticationDto changePassword(UUID id, ChangePasswordDto changePasswordDto) {
		User user = modelMapper.map(userService.retrieveById(id), User.class);
		if(passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())){
			user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
			userService.save(user);
			return createAuthenticationObject(user);
		} else {
			throw new UnauthorizedException(ExceptionConstant.USER_E003);
		}
	}

	@Override
	public UserDto resetPassword(ResetPasswordDto resetPasswordDto) {
		if(resetPasswordDto.getOtp().equals(otpResponseDto.getOtp())){
			if(Duration.between(otpResponseDto.getOtpGeneratedTime(),
					LocalDateTime.now()).getSeconds() < AppConstant.OTP_EXPIRATION_TIME){
				User user = userService.retrieveByEmail(resetPasswordDto.getEmail());
				user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
				return userService.save(user);
			} else {
				throw new BadRequestException(ExceptionConstant.USER_E008);
			}
		} else {
			throw new NotFoundException(ExceptionConstant.USER_E006);
		}
	}

	@Override
	public JWTAuthenticationDto refresh(RefreshTokenDto refreshTokenDto) {
		String userEmail = jwtService.extractUserName(refreshTokenDto.getToken());
		User user = userService.retrieveByEmail(userEmail);
		if(jwtService.isTokenValid(refreshTokenDto.getToken(), user)){
			String jwt = jwtService.generateToken(user);
			JWTAuthenticationDto jwtAuthenticationDto = userMapper.convertUserToJWTAuthenticationResponse(user);
			jwtAuthenticationDto.setToken(jwt);
			jwtAuthenticationDto.setRefreshToken(refreshTokenDto.getToken());
			return jwtAuthenticationDto;
		}
		return null;
	}

	@Override
	public JWTAuthenticationDto createAuthenticationObject(User user) {
		String jwt = jwtService.generateToken(user);
		String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
		JWTAuthenticationDto jwtAuthenticationDto = userMapper.convertUserToJWTAuthenticationResponse(user);
		jwtAuthenticationDto.setToken(jwt);
		jwtAuthenticationDto.setRefreshToken(refreshToken);
		return jwtAuthenticationDto;
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
