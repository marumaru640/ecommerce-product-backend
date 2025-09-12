package com.marumaru640.ecommerce_product_backend.health;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "https://ecommerce-product-azure.vercel.app/")
@RestController
@RequestMapping("/api")
public class HealthController {
	@GetMapping("/health")
	public String health() {
		return "Ok";
	}
}
