package com.marumaru640.ecommerce_product_backend.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
	public static final String RT_COOKIE = "RefreshToken";
	
	public static void addRefreshTokenCookie(HttpServletResponse res, String token, int maxAgeSeconds) {
		Cookie cookie = new Cookie(RT_COOKIE, token);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeSeconds);
		
		String headerValue = String.format(
				"%s=%s; Path=/; Max-Age=%d; HttpOnly; Secure; SameSite=None", 
				RT_COOKIE, token, maxAgeSeconds);
		
		res.addHeader("Set-cookie", headerValue);
	}
	
	public static void clearRefreshTokenCookie(HttpServletResponse res) {
		Cookie cookie = new Cookie(RT_COOKIE, "");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		res.addCookie(cookie);
	}
	
	public static String extractRefreshToken(HttpServletRequest req) {
		if(req.getCookies() == null) return null;
		for(Cookie cookie : req.getCookies()) {
			if(RT_COOKIE.equals(cookie.getName())) return cookie.getValue();
		}
		
		return null;
	}
}
