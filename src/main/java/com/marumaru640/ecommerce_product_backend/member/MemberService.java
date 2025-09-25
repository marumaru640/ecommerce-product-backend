package com.marumaru640.ecommerce_product_backend.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.marumaru640.ecommerce_product_backend.excepton.EmailAlreadyExistsException;
import com.marumaru640.ecommerce_product_backend.excepton.NotFoundException;
import com.marumaru640.ecommerce_product_backend.mapper.MemberMapper;
import com.marumaru640.ecommerce_product_backend.member.dto.MemberCreateRequest;
import com.marumaru640.ecommerce_product_backend.member.dto.MemberResponse;
import com.marumaru640.ecommerce_product_backend.member.dto.MemberUpdateRequest;

import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
	
	private final MemberRepository repo;
	private final MemberMapper mapper;
	private final PasswordEncoder passwordEncoder;
	
	public MemberService(MemberRepository repo, MemberMapper mapper, PasswordEncoder passwordEncoder) {
		this.repo = repo;
		this.mapper = mapper;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Transactional
	public MemberResponse get(Long id) {
		Member m = repo.findById(id).orElseThrow();
		return mapper.toResponse(m);
	}
	
	@Transactional
	public MemberResponse create(MemberCreateRequest req) {
		if(repo.existsByEmail(req.email())) {
			throw new EmailAlreadyExistsException("Email is already taken: " + req.email());
		}
		
		Member entity = mapper.toCreateEntity(req);
		
		String hashedPassword = passwordEncoder.encode(entity.getPassword()); 
		entity.setPassword(hashedPassword);
		Member saved = repo.save(entity);
		return mapper.toResponse(saved);
	}
	
	@Transactional
	public MemberResponse update(Long id, MemberUpdateRequest req) {
		Member entity = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));
		
		mapper.updateEntityFromDto(req, entity);
		
		if(req.newPassword() != null && !req.newPassword().isBlank()) {
			entity.setPassword(passwordEncoder.encode(req.newPassword()));
		}
		
		Member saved = repo.save(entity);
		return mapper.toResponse(saved);
	}
	
	@Transactional
	public void delete(Long id) {
		if(!repo.existsById(id)) {
			throw new NotFoundException("Member not found with id: " + id);
		}
		
		repo.deleteById(id);
	}
}
