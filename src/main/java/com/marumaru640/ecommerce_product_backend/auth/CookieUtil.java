package com.marumaru640.ecommerce_product_backend.auth;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
	public static final String RT_COOKIE = "RefreshToken";
	

	
	public static void addRefreshTokenCookie(
			HttpServletResponse res, 
			String token, 
			int maxAgeSeconds,
			boolean secure,
			String sameSite
			) {
		ResponseCookie cookie = ResponseCookie.from(RT_COOKIE, token)
				.httpOnly(true)
				.secure(secure)
				.path("/")
				.maxAge(maxAgeSeconds)
				.sameSite(sameSite)
				.build();
		
		res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
	
	public static void clearRefreshTokenCookie(
			HttpServletResponse res, 			
			boolean secure,
			String sameSite
			) {
		ResponseCookie cookie = ResponseCookie.from(RT_COOKIE, "")
				.httpOnly(true)
				.secure(secure)
				.path("/")
				.maxAge(Duration.ZERO)
				.sameSite(sameSite)
				.build();
		
		res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
	
	public static String extractRefreshToken(HttpServletRequest req) {
		if(req.getCookies() == null) return null;
		for(Cookie cookie : req.getCookies()) {
			if(RT_COOKIE.equals(cookie.getName())) return cookie.getValue();
		}
		
		return null;
	}
}
