package com.example.demo.product.dto.response;

import lombok.Data;


// * 상품 통화 응답 DTO 통화 정보

@Data
public class ProductCurrencyResponseDto {

    private Long productCurrencyId;//상품 통화 ID

    private Long productId; // 상품 ID

    /**
     * 통화 코드
     * USD / JPY / EUR
     */
    private String currencyCode;

    private String currencyName; // 통화명

    private String isAvailable; // 사용가능 여부
}