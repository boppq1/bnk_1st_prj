package com.example.demo.product.dto.response;

import lombok.Data;

import java.math.BigDecimal;

// 우대금리 응답 DTO, 우대 금리 조건
@Data
public class PreferentialRateResponseDto {

    private Long prefRateId; // 우대금리 ID

    private Long rateId; // 금리 ID

    private String conditionType; // 우대 조건 유형

    private String conditionDesc;// 우대 조건 설명

    private BigDecimal prefRate; //우대 금리
}