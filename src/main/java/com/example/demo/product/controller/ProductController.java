package com.example.demo.product.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.product.dto.request.ProductListRequestDto;
import com.example.demo.product.dto.response.ProductDetailResponseDto;
import com.example.demo.product.dto.response.ProductListResponseDto;
import com.example.demo.product.dto.response.ProductPageResponseDto;
import com.example.demo.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 전체 상품 리스트
    // 예: /api/products?page=1&size=10
    @GetMapping
    public ProductPageResponseDto getProducts(ProductListRequestDto dto) {
        return productService.getProductList(dto);
    }

    // 개인 상품 리스트
    // 예: /api/products/personal?page=1&size=10
    @GetMapping("/personal")
    public ProductPageResponseDto getPersonalProducts(ProductListRequestDto dto) {
        dto.setTargetCustomer("personal");
        return productService.getProductList(dto);
    }

    // 기업 상품 리스트
    // 예: /api/products/company?page=1&size=10
    @GetMapping("/company")
    public ProductPageResponseDto getCompanyProducts(ProductListRequestDto dto) {
        dto.setTargetCustomer("company");
        return productService.getProductList(dto);
    }

	 // 추천 상품
	 // 예: /api/products/recommend?targetCustomer=personal
	 @GetMapping("/recommend")
	 public List<ProductListResponseDto> getRecommendProducts(
	         @RequestParam("targetCustomer") String targetCustomer
	 ) {
	     return productService.getRecommendProducts(targetCustomer);
	 }
	
	 // 상품 상세
	 // 예: /api/products/1
	 @GetMapping("/{productId}")
	 public ProductDetailResponseDto getProductDetail(
	         @PathVariable("productId") Long productId
	 ) {
	     return productService.getProductDetail(productId);
	 }
}