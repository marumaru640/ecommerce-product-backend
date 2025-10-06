package com.marumaru640.ecommerce_product_backend.auth;



import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.marumaru640.ecommerce_product_backend.exception.NotFoundException;
import com.marumaru640.ecommerce_product_backend.member.Member;
import com.marumaru640.ecommerce_product_backend.member.MemberRepository;
import com.marumaru640.ecommerce_product_backend.member.RefreshToken;
import com.marumaru640.ecommerce_product_backend.member.RefreshTokenRepository;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {
	
	@Value("${app.jwt.refresh-expires:7}")
	private long refreshExpiresDays;
	
	private final RefreshTokenRepository refreshRepo;
	private final MemberRepository memberRepo;
	
	public RefreshTokenService(RefreshTokenRepository refreshRepo, MemberRepository memberRepo) {
		this.refreshRepo = refreshRepo;
		this.memberRepo = memberRepo;
	}
	
	@Transactional
	public RefreshToken createRefreshToken(Long memberId) {
		Member member = memberRepo.findById(memberId)
				.orElseThrow(() -> new NotFoundException("Member not found"));
		
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setMember(member);
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setExpriyDate(Instant.now().plus(refreshExpiresDays, ChronoUnit.DAYS));
		
		return refreshRepo.save(refreshToken);
	}
	
	public RefreshToken verifyRefreshToken(String token) {
		RefreshToken refreshToken = refreshRepo.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Refresh token not found in DB"));
		
		if (refreshToken.getExpriyDate().isBefore(Instant.now())) {
			refreshRepo.delete(refreshToken);
			throw new RuntimeException("Refresh token was expired. Please make a new sigin request.");
		}
		
		return refreshToken;
	}
}
