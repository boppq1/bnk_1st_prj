package com.example.demo.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.admin.dto.ProductDto;
import com.example.demo.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminProductController {
	
	final AdminService as;
	
	// 세션에서 관리자 정보 받아서 넘기기
	@GetMapping("/makeProductPage")
	public String makeProductPage() {
		return "admin/makeProductPage";
	}
	
	// created_by, updated_by은 추후에 세션에 저장된 아이디 넣으면 됨 
	@PostMapping("/product")
	public String makeProduct(ProductDto dto) {
		as.makeProduct(dto);
		return "";
	}
	
	@GetMapping("/product")
	public String getProduct(@RequestParam("product_id") Long product_id) {
		as.getProduct(product_id);
		return "";
	}
	
	@GetMapping("/products")
	public String getProducts() {
		as.getProducts();
		return "";
	}
}
