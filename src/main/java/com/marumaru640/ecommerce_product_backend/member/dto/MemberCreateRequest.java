package com.marumaru640.ecommerce_product_backend.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberCreateRequest (
		@NotBlank @Email String email,
		@NotBlank String password, 
		@NotBlank String name) {}