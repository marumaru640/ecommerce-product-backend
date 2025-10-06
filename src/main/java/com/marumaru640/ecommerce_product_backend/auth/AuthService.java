package com.marumaru640.ecommerce_product_backend.auth;

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

@Service
public class AuthService {
	private final MemberRepository memberRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenService jwtTokenService;
	private final RefreshTokenService refreshTokenService;
	private final AuthenticationManager authenticationManager;
	
	
	public AuthService (
			MemberRepository memberRepo,
			PasswordEncoder passwordEncoder,
			JwtTokenService jwtTokenService,
			RefreshTokenService refreshTokenService,
			AuthenticationManager authenticationManager
			) {
		this.memberRepo = memberRepo;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenService = jwtTokenService;
		this.refreshTokenService = refreshTokenService;
		this.authenticationManager = authenticationManager;
	}
	
	@Transactional
	public LoginResponse login(String email, String rawPassword) {
		Authentication auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, rawPassword)
				);
		
		Member member = memberRepo.findByEmail(email).orElseThrow();
		
		String accessToken = jwtTokenService.generateToken(member);
		
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(member.getId());
		
		return new LoginResponse(
				accessToken,
				refreshToken.getToken(),
				"Bearer", 
				jwtTokenService.expiresInSeconds()
				);
	}
	
	@Transactional
	public LoginResponse refreshToken(String refreshTokenString) {
		RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenString);
		
		Member member = refreshToken.getMember();
		
		String newAccessToken = jwtTokenService.generateToken(member);
		
		return new LoginResponse(
				newAccessToken,
				refreshTokenString,
				"Bearer",
				jwtTokenService.expiresInSeconds()
				);
	}
}
