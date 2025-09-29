package com.marumaru640.ecommerce_product_backend.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marumaru640.ecommerce_product_backend.exception.UnauthorizedException;
import com.marumaru640.ecommerce_product_backend.member.MemberRepository;
import com.marumaru640.ecommerce_product_backend.member.dto.LoginResponse;

@Service
public class AuthService {
	private final MemberRepository repo;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenService jwtTokenService;
	
	public AuthService (
			MemberRepository repo,
			PasswordEncoder passwordEncoder,
			JwtTokenService jwtTokenService
			) {
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenService = jwtTokenService;
	}
	
	@Transactional(readOnly = true) 
	public LoginResponse login(String email, String rawPassword) {
		var member = repo.findByEmail(email)
				.orElseThrow(() -> new UnauthorizedException("Invalid email or password"));
		
		if(!passwordEncoder.matches(rawPassword, member.getPassword())) {
			throw new UnauthorizedException("Invalid password");
		}
		
		var token = jwtTokenService.generateToken(member);
		return new LoginResponse(token, "Bearer", jwtTokenService.expiresInSeconds());
	}
}
