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
		refreshToken.setRevoked(false);
		
		return refreshRepo.save(refreshToken);
	}
	
	@Transactional
	public RefreshToken verifyUsable(String token) {
		RefreshToken refreshToken = refreshRepo.findByTokenAndRevokedFalse(token)
				.orElseThrow(() -> new RuntimeException("Refresh token not found in DB"));
		
		if (refreshToken.getExpriyDate().isBefore(Instant.now())) {
			refreshToken.setRevoked(true);
			refreshRepo.save(refreshToken);
			throw new RuntimeException("Refresh token was expired. Please make a new sigin request.");
		}
		
		return refreshToken;
	}
	
	@Transactional
	public RefreshToken rotate(RefreshToken old) {
		old.setRevoked(true);
		refreshRepo.save(old);
		
		RefreshToken newRefreshToken = new RefreshToken();
		newRefreshToken.setMember(old.getMember());
		newRefreshToken.setToken(UUID.randomUUID().toString());
		newRefreshToken.setExpriyDate(Instant.now().plus(refreshExpiresDays, ChronoUnit.DAYS));
		newRefreshToken.setRevoked(false);
		
		return refreshRepo.save(newRefreshToken);
	}
	
	@Transactional
	public void revoke(String token) {
		refreshRepo.findByToken(token).ifPresent(rt -> {
			rt.setRevoked(true);
			refreshRepo.save(rt);
		});
	}
}
