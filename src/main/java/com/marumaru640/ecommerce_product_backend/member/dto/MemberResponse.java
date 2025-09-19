package com.marumaru640.ecommerce_product_backend.member.dto;

import java.time.LocalDateTime;

public record MemberResponse (Long id, String email, String name, LocalDateTime createdAt) {}