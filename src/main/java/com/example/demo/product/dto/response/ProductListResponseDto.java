package com.example.demo.product.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

// 상품 리스트 카드 데이터. 상품 카드에 보이는 것만
@Data
public class ProductListResponseDto {

    private Long productId; // 상품 ID

    private String productName; // 상품명

    private String productType; // 상품 유형

    private String targetLarge; // 고객 유형: PERSONAL, COMPANY, ALL

    private String targetDetail; // 고객 소분류: ALL, YOUTH, SENIOR, CHILD 등

    private String productDesc; // 상품 설명

    private String rateCategory; // 금리 카테고리

    private Integer minPeriodMonth; // 최소 가입 기간

    private Integer maxPeriodMonth; // 최대 가입 기간

    private Long minAmount; // 최소 가입 금액

    private Long maxAmount; // 최대 가입 금액

    private BigDecimal minRate; // 최소 금리

    private BigDecimal maxRate; // 최대 금리

    private LocalDate appStartDate; // 신청 시작일

    private LocalDate appEndDate; // 신청 마감일
}