package com.marumaru640.ecommerce_product_backend.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marumaru640.ecommerce_product_backend.member.dto.LoginRequest;
import com.marumaru640.ecommerce_product_backend.member.dto.LoginResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
		var res = authService.login(req.email(), req.password());
		return ResponseEntity.ok(res);
	}
}
