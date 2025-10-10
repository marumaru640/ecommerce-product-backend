package com.marumaru640.ecommerce_product_backend.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marumaru640.ecommerce_product_backend.member.dto.LoginRequest;
import com.marumaru640.ecommerce_product_backend.member.dto.LoginResponse;
import com.marumaru640.ecommerce_product_backend.member.dto.RefreshTokenRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req, HttpServletResponse res) {
		var resDto = authService.login(req.email(), req.password(), res);
		return ResponseEntity.ok(resDto);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest req, HttpServletResponse res) {
		var resDto = authService.refreshToken(req, res);
		return ResponseEntity.ok(resDto);
	}
}
