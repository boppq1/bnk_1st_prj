package com.example.demo.exchange.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ExchangeRateResponseDto {

    private Long rateNo;

    private String currencyCode;     // DB 원본 통화코드: USD, JPY(100), IDR(100)
    private String displayCurrencyCode; // 화면/계산용 코드: USD, JPY, IDR

    private BigDecimal ttb;          // 매수 환율
    private BigDecimal tts;          // 매도 환율
    private BigDecimal dealBaseRate; // 매매 기준율

    private Integer currencyUnit;    // 1 또는 100

    private LocalDateTime noticeDate;
}