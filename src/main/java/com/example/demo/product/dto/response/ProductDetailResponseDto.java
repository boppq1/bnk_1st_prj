package com.example.demo.product.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

// 상품 상세 페이지 전체 데이터
@Data
public class ProductDetailResponseDto {

    private Long productId; // 상품 ID

    private String isActive; // 판매 여부

    private String productName; // 상품명

    private String productType; // 상품 유형

    private String targetLarge; // 고객 유형 대분류

    private String targetDetail; // 고객 유형 소분류

    private Integer minPeriodMonth; // 최소 가입 기간

    private Integer maxPeriodMonth; // 최대 가입 기간

    private Long minAmount; // 최소 가입 금액

    private Long maxAmount; // 최대 가입 금액

    private String productDesc; // 상품 설명

    private String interestPaymentType; // 이자 지급 방식

    private String appliedExchangeRateType; // 적용 환율 기준

    private LocalDate appStartDate; // 신청 시작일

    private LocalDate appEndDate; // 신청 종료일

    private String rateCategory; // 금리 카테고리

    private String approveStatus; // 승인 상태

    private List<ProductCurrencyResponseDto> currencies; // 통화 리스트

    private List<ProductRateResponseDto> rates; // 금리 리스트

    private List<PreferentialRateResponseDto> preferentialRates; // 우대 금리 리스트
}