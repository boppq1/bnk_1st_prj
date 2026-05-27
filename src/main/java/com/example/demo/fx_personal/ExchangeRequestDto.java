package com.example.demo.fx_personal;

import java.math.BigDecimal;

/**
 * 프론트에서 환전 신청 시 넘어오는 요청 데이터
 */
public class ExchangeRequestDto {

    private String dealType;       // "BUY"(외화 살 때) / "SELL"(외화 팔 때)
    private String currency;       // 거래 통화 코드 (USD, JPY(100) 등)
    private BigDecimal amount;     // 사용자가 입력한 외화 금액 (외화 기준)

    public ExchangeRequestDto() {}

    public String getDealType() { return dealType; }
    public void setDealType(String dealType) { this.dealType = dealType; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}