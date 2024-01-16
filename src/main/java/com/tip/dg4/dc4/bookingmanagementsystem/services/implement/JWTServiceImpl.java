package com.tip.dg4.dc4.bookingmanagementsystem.services.implement;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.tip.dg4.dc4.bookingmanagementsystem.services.JWTService;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.AppConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * JWTServiceImpl
 *
 * @author Tuan Le / DG4 Team<br>
 * Created on 11/29/2023
 * @since 1.0.0
 */
@Service
public class JWTServiceImpl implements JWTService {
	@Override
	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	@Override
	public String generateToken(UserDetails userDetails) {
		return Jwts.builder().setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + AppConstant.TOKEN_EXPIRATION_TIME))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	@Override
	public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.builder().setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + AppConstant.REFRESH_TOKEN_EXPIRATION_TIME))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	@Override
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	@Override
	public boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}

	private Key getSignInKey(){
		byte[] key = Decoders.BASE64.decode(AppConstant.SECRET);
		return Keys.hmacShaKeyFor(key);
	}

	private <T> T extractClaim (String token, Function<Claims, T> claimsResolver){
		final Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey())
				.build().parseClaimsJws(token).getBody();
		return claimsResolver.apply(claims);
	}
}
