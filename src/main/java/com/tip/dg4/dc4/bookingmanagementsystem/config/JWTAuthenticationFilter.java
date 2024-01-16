package com.tip.dg4.dc4.bookingmanagementsystem.config;

import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.BadRequestException;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.UnauthorizedException;
import com.tip.dg4.dc4.bookingmanagementsystem.services.JWTService;
import com.tip.dg4.dc4.bookingmanagementsystem.services.UserService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.ExceptionConstant;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;

/**
 * JWTAuthenticationFilter
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/29/2023
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
	private final JWTService jwtService;
	private final UserService userService;
	private final HandlerExceptionResolver handlerExceptionResolver;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;

		try {
			if(StringUtils.isEmpty(authHeader) || !org.apache.commons.lang3.StringUtils.startsWith(authHeader, AppConstant.BEARER)){
				filterChain.doFilter(request, response);
				return;
			}

			jwt = authHeader.substring(7);

			userEmail = jwtService.extractUserName(jwt);

			if(StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null){
				UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);

				if(jwtService.isTokenValid(jwt, userDetails)){
					SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities()
					);
					token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					securityContext.setAuthentication(token);
					SecurityContextHolder.setContext(securityContext);
				}
			}
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e){
			handlerExceptionResolver.resolveException(request, response, null, new UnauthorizedException(ExceptionConstant.USER_E012));
		} catch (SignatureException e){
			handlerExceptionResolver.resolveException(request, response, null, new UnauthorizedException(ExceptionConstant.USER_E013));
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return Arrays.stream(AppConstant.AUTH_WHITELIST).anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
	}
}
