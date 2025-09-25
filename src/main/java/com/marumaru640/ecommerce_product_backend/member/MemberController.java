package com.marumaru640.ecommerce_product_backend.member;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marumaru640.ecommerce_product_backend.member.dto.MemberCreateRequest;
import com.marumaru640.ecommerce_product_backend.member.dto.MemberResponse;
import com.marumaru640.ecommerce_product_backend.member.dto.MemberUpdateRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/members")
public class MemberController {
	private final MemberService service;
	
	public MemberController(MemberService service) {
		this.service = service;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<MemberResponse> get(@PathVariable Long id) {
		MemberResponse responseBody = service.get(id);
		return new ResponseEntity<MemberResponse>(responseBody, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<MemberResponse> create(@RequestBody @Valid MemberCreateRequest req) {
		MemberResponse responseBody = service.create(req);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
		
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<MemberResponse> update(@PathVariable Long id, @RequestBody MemberUpdateRequest req) {
		MemberResponse responseBody = service.update(id, req);
		return ResponseEntity.ok(responseBody);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
