package com.example.demo.product.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WonInterestCalcRequestDto {

    private Long productId;          // 상품 ID

    private Long amount;             // 가입금액, 원화

    private BigDecimal rate;         // 상품 금리

    private Integer periodMonth;     // 가입기간, 개월
}