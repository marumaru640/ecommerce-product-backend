package com.marumaru640.ecommerce_product_backend.auth;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marumaru640.ecommerce_product_backend.exception.UnauthorizedException;
import com.marumaru640.ecommerce_product_backend.member.Member;
import com.marumaru640.ecommerce_product_backend.member.MemberRepository;
import com.marumaru640.ecommerce_product_backend.member.RefreshToken;
import com.marumaru640.ecommerce_product_backend.member.dto.LoginResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {
	private final MemberRepository memberRepo;
	private final JwtTokenService jwtTokenService;
	private final RefreshTokenService refreshTokenService;
	private final AuthenticationManager authenticationManager;
	
	@Value("${app.cookie.secure}") 
	private boolean cookieSecure;
	
	@Value("${app.cookie.same-site}") 
	private String cookieSameSite;
	
	
	public AuthService (
			MemberRepository memberRepo,
			PasswordEncoder passwordEncoder,
			JwtTokenService jwtTokenService,
			RefreshTokenService refreshTokenService,
			AuthenticationManager authenticationManager
			) {
		this.memberRepo = memberRepo;
		this.jwtTokenService = jwtTokenService;
		this.refreshTokenService = refreshTokenService;
		this.authenticationManager = authenticationManager;
	}
	
	@Transactional
	public LoginResponse login(String email, String rawPassword, HttpServletResponse res) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, rawPassword)
				);
		
		Member member = memberRepo.findByEmail(email).orElseThrow();
		
		String accessToken = jwtTokenService.generateToken(member);
		
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(member.getId());
		
		CookieUtil.addRefreshTokenCookie(res, refreshToken.getToken(), 
				(int) (refreshToken.getExpriyDate().getEpochSecond() - Instant.now().getEpochSecond()), 
				cookieSecure,
				cookieSameSite
				);
		
		return new LoginResponse(
				accessToken,
				"Bearer", 
				jwtTokenService.expiresInSeconds()
				);
	}
	
	@Transactional
	public LoginResponse refreshToken(HttpServletRequest req, HttpServletResponse res) {
		String refreshTokenString = CookieUtil.extractRefreshToken(req);
		if(refreshTokenString == null || refreshTokenString.isBlank()) {
			throw new UnauthorizedException("No refresh token cookie");
		}
		
		RefreshToken valid = refreshTokenService.verifyUsable(refreshTokenString);
		
		Member member = valid.getMember();
		
		String newAccessToken = jwtTokenService.generateToken(member);
		
		RefreshToken rotated = refreshTokenService.rotate(valid);
		
		CookieUtil.addRefreshTokenCookie(res, rotated.getToken(), 
				(int) (rotated.getExpriyDate().getEpochSecond() - Instant.now().getEpochSecond()),
				cookieSecure,
				cookieSameSite
				);
		
		return new LoginResponse(
				newAccessToken,
				"Bearer",
				jwtTokenService.expiresInSeconds()
				);
	}
	
	@Transactional
	public void logout(HttpServletRequest req, HttpServletResponse res) {
		String refreshTokenString = CookieUtil.extractRefreshToken(req);
		if(refreshTokenString != null) {
			refreshTokenService.revoke(refreshTokenString);
		}
		
		CookieUtil.clearRefreshTokenCookie(res,cookieSecure,cookieSameSite);
	}
}
