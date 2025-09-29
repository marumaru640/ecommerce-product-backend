package com.marumaru640.ecommerce_product_backend.auth;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.marumaru640.ecommerce_product_backend.member.Member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenService {
	
	private final SecretKey key;
	private final String issuer;
	private final long expiresMinutes;
	
	public JwtTokenService(
			@Value("${app.jwt.secret}") String secret,
			@Value("${app.jwt.issuer:ecommerce-product}") String issuer,
			@Value("${app.jwt.expires-minutes:60}") long expiresMinutes
			) {
		if(secret == null || secret.isBlank()) {
			throw new IllegalAccessError("JWT secret is not configured (app.jwt.secret / JWT_SECRET).");
		}
		
		byte[] bytes = Base64.getDecoder().decode(secret);
		this.key = Keys.hmacShaKeyFor(bytes);
		this.issuer = issuer;
		this.expiresMinutes = expiresMinutes;
	}
	
	public String generateToken(Member m) {
		Instant now = Instant.now();
		Instant exp = now.plus(expiresMinutes, ChronoUnit.MINUTES);
		
		return Jwts.builder()
				.setIssuer(issuer)
				.setSubject(m.getId().toString())
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(exp))
				.addClaims(Map.of(
						"email", m.getEmail(),
						"name", m.getName()
						))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public long expiresInSeconds() {
		return expiresMinutes * 60;
	}
}
