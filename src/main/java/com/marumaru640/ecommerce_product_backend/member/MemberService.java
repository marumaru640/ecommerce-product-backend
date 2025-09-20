package com.marumaru640.ecommerce_product_backend.member;

import org.springframework.stereotype.Service;

import com.marumaru640.ecommerce_product_backend.mapper.MemberMapper;
import com.marumaru640.ecommerce_product_backend.member.dto.MemberCreateRequest;
import com.marumaru640.ecommerce_product_backend.member.dto.MemberResponse;

import jakarta.transaction.Transactional;

@Service
public class MemberService {
	
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
		Member entity = mapper.toEntity(req);
		Member saved = repo.save(entity);
		return mapper.toResponse(saved);
	}
}
