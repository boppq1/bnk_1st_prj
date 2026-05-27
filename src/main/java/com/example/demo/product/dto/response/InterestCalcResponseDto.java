package com.example.demo.product.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterestCalcResponseDto {

    private String calcType;              // WON 또는 FOREIGN

    private String currencyCode;          // KRW, USD, JPY 등

    private Long inputKrwAmount;          // 입력한 원화 금액

    private BigDecimal appliedRate;       // 적용 금리

    private Integer periodMonth;          // 가입기간

    private BigDecimal exchangeRate;      // 적용 환율

    private BigDecimal foreignPrincipal;  // 외화 원금

    private BigDecimal beforeTaxInterest; // 세전 이자

    private BigDecimal afterTaxInterest;  // 세후 이자

    private BigDecimal maturityForeignAmount; // 만기 외화 금액

    private BigDecimal maturityKrwAmount;     // 예상 원화 수령액

    private String message;               // 안내 문구
}