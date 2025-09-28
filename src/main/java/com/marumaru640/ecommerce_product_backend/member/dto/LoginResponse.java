package com.marumaru640.ecommerce_product_backend.member.dto;

public record LoginResponse (
		String token, 
		String tokenType, 
		long expiresInSeconds) {}
