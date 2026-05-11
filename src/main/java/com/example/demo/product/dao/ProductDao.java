package com.example.demo.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.product.dto.request.ProductListRequestDto;
import com.example.demo.product.dto.response.PreferentialRateResponseDto;
import com.example.demo.product.dto.response.ProductCurrencyResponseDto;
import com.example.demo.product.dto.response.ProductDetailResponseDto;
import com.example.demo.product.dto.response.ProductListResponseDto;
import com.example.demo.product.dto.response.ProductRateResponseDto;

@Mapper
public interface ProductDao {
	// 상품 리스트
    List<ProductListResponseDto> getProductList(ProductListRequestDto dto);
    
    // 상품 갯수
    int getProductCount(ProductListRequestDto dto);
    
    // 상품 상세
    ProductDetailResponseDto getProductDetail(Long productId);
    
    // 통화 정보
    List<ProductCurrencyResponseDto> getCurrencies(Long productId);

    // 금리 정보
    List<ProductRateResponseDto> getRates(Long productId);

    // 우대금리 정보
    List<PreferentialRateResponseDto> getPreferentialRates(Long productId);

    // 추천 상품
    List<ProductListResponseDto> getRecommendProducts(String targetCustomer);
}
