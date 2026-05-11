package com.example.demo.product.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

// 상품 금리 응답 DTO, 금리 정보

@Data
public class ProductRateResponseDto {
    private Long rateId; // 금리 ID
    private Long productId; //상품 ID
    private String currencyCode;// 통화 코드
    private String customerType;// 고객 유형
    private String residencyType;// 거주자 유형
    private Integer periodMonth;// 가입 기간
    private BigDecimal baseRate; // 기본 금리
    private BigDecimal preferentialRate; //우대금리
    private BigDecimal maxRate; // 최대 금리
    private BigDecimal maturityRate; // 만기후 금리
    private BigDecimal earlyCancelRate; // 중도 해지 금리
    private LocalDate rateStartDate;// 금리 적용 시작일
    private LocalDate rateEndDate;// 금리 적용 종료일
    private String isActive; // 활성 여부
    private String rateType;// 금리 타입: 고정 / 변동
}