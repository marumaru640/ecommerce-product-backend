package com.marumaru640.ecommerce_product_backend.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.marumaru640.ecommerce_product_backend.member.Member;
import com.marumaru640.ecommerce_product_backend.member.MemberRepository;

@Service
public class MemberUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepo;

    public MemberUserDetailsService(MemberRepository memberRepo) {
        this.memberRepo = memberRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member m = memberRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return org.springframework.security.core.userdetails.User
                .withUsername(m.getEmail())
                .password(m.getPassword())   // ここはハッシュ化済み文字列
                .authorities("ROLE_USER")
                .accountExpired(false).accountLocked(false)
                .credentialsExpired(false).disabled(false)
                .build();
    }
}
