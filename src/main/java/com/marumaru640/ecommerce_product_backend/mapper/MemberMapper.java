package com.marumaru640.ecommerce_product_backend.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.marumaru640.ecommerce_product_backend.member.Member;
import com.marumaru640.ecommerce_product_backend.member.dto.MemberCreateRequest;
import com.marumaru640.ecommerce_product_backend.member.dto.MemberResponse;

@Mapper(componentModel = "spring")
public interface MemberMapper {
	MemberResponse toResponse(Member m);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	Member toEntity(MemberCreateRequest req);
}
