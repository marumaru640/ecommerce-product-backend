package com.marumaru640.ecommerce_product_backend.member;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "members", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Setter @NotBlank @Email
	@Column(nullable = false, length = 255)
	private String email;
	
	@JsonIgnore
	@Setter @NotBlank
	@Column(nullable = false, length = 255)
	private String password;
	
	@Setter @NotBlank
	@Column(nullable = false, length = 100)
	private String name;
	
	@Column(nullable = false)
	private LocalDateTime createdAt;
	
	@PrePersist
	void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
	
	public Member() {
	}

	public Member(String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}
}
