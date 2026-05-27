package com.example.demo.product.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ForeignInterestCalcRequestDto {

    private Long productId;           // 상품 ID

    private Long krwAmount;           // 사용자가 입력한 원화 금액

    private String currencyCode;      // USD, JPY, EUR 등

    private BigDecimal rate;          // 상품 금리, 예: 1.9

    private Integer periodMonth;      // 가입기간, 개월

    private BigDecimal expectedRate;  // 만기 예상 환율, 사용자가 입력 안 하면 현재 환율 사용
}