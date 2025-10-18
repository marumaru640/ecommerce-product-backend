package com.marumaru640.ecommerce_product_backend.config;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Value("${app.cors.allowed-origins}")
	private List<String> allowedOrigins;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.httpBasic(basic -> basic.disable())
			.formLogin(form -> form.disable())
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authz -> authz
					.requestMatchers(
							"/api/members",
							"/api/auth/login", 
							"/api/health", 
							"/api/auth/refresh", 
							"/api/auth/logout"
							).permitAll()
					.anyRequest().authenticated()
			);
		return http.build();
	}
	
	@Bean
	  public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration conf = new CorsConfiguration();
		conf.setAllowedOrigins(allowedOrigins);
	    conf.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
	    conf.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-Requested-With", "Accept"));
	    conf.setAllowCredentials(true); // Cookie送信を許可
	    conf.setMaxAge(Duration.ofHours(1));
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", conf);
	    return source;
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
	    return configuration.getAuthenticationManager();
	}
}