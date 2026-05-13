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
    @GetMapping
    public ProductPageResponseDto getProducts(ProductListRequestDto dto) {
        return productService.getProductList(dto);
    }

    // 개인 상품 리스트
    @GetMapping("/personal")
    public ProductPageResponseDto getPersonalProducts(ProductListRequestDto dto) {
        dto.setTargetLarge("PERSONAL");
        return productService.getProductList(dto);
    }

    // 기업 상품 리스트
    @GetMapping("/company")
    public ProductPageResponseDto getCompanyProducts(ProductListRequestDto dto) {
        dto.setTargetLarge("COMPANY");
        return productService.getProductList(dto);
    }

    // 추천 상품
    // 예: /api/products/recommend?targetLarge=PERSONAL
    @GetMapping("/recommend")
    public List<ProductListResponseDto> getRecommendProducts(
            @RequestParam("targetLarge") String targetLarge
    ) {
        ProductListRequestDto dto = new ProductListRequestDto();
        dto.setTargetLarge(targetLarge.toUpperCase());

        return productService.getRecommendProducts(dto);
    }

    // 상품 상세
    @GetMapping("/{productId}")
    public ProductDetailResponseDto getProductDetail(
            @PathVariable("productId") Long productId
    ) {
        return productService.getProductDetail(productId);
    }
}