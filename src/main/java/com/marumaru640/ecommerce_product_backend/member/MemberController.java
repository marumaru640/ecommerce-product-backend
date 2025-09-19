package com.marumaru640.ecommerce_product_backend.member;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marumaru640.ecommerce_product_backend.member.dto.MemberCreateRequest;
import com.marumaru640.ecommerce_product_backend.member.dto.MemberResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/members")
public class MemberController {
	private final MemberService service;
	
	public MemberController(MemberService service) {
		this.service = service;
	}
	
	@GetMapping("/{id}")
	public MemberResponse get(@PathVariable Long id) {
		return service.get(id);
	}
	
	@PostMapping
	public MemberResponse create(@RequestBody @Valid MemberCreateRequest req) {
		return service.create(req);
	}
}
