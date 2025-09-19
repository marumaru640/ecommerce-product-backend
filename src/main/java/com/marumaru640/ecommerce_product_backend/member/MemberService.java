package com.marumaru640.ecommerce_product_backend.member;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.marumaru640.ecommerce_product_backend.mapper.MemberMapper;
import com.marumaru640.ecommerce_product_backend.member.dto.MemberCreateRequest;
import com.marumaru640.ecommerce_product_backend.member.dto.MemberResponse;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberService {
	
	Logger logger = LoggerFactory.getLogger(MemberService.class);
	
	private final MemberRepository repo;
	private final MemberMapper mapper;
	
	public MemberService(MemberRepository repo, MemberMapper mapper) {
		this.repo = repo;
		this.mapper = mapper;
	}
	
	@Transactional
	public MemberResponse get(Long id) {
		Member m = repo.findById(id).orElseThrow();
		return mapper.toResponse(m);
	}
	
	@Transactional
	public MemberResponse create(MemberCreateRequest req) {
		logger.debug("req email={}, name={}, pwd={}", req.email(), req.name(), req.password());
		Member entity = mapper.toEntity(req);
		logger.debug("mapped -> email={}, name={}, paws={}",entity);
		Member saved = repo.save(entity);
		return mapper.toResponse(saved);
	}
}
