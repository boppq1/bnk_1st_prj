package com.example.demo.fx_personal;

import java.math.BigDecimal;

/**
 * 환전 처리 결과를 프론트로 돌려주는 응답 데이터
 */
public class ExchangeResultDto {

    private boolean success;          // 처리 성공 여부
    private String message;           // 안내/에러 메시지
    private String dealType;          // BUY / SELL
    private String currency;          // 거래 통화
    private BigDecimal foreignAmount; // 외화 금액
    private BigDecimal krwAmount;     // 원화 금액
    private BigDecimal appliedRate;   // 적용 환율
    private String exchCpltDt;        // 환전 완료 시각 (표시용 문자열)

    public ExchangeResultDto() {}

    /** 실패 응답 빠른 생성 */
    public static ExchangeResultDto fail(String message) {
        ExchangeResultDto r = new ExchangeResultDto();
        r.success = false;
        r.message = message;
        return r;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getDealType() { return dealType; }
    public void setDealType(String dealType) { this.dealType = dealType; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getForeignAmount() { return foreignAmount; }
    public void setForeignAmount(BigDecimal foreignAmount) { this.foreignAmount = foreignAmount; }

    public BigDecimal getKrwAmount() { return krwAmount; }
    public void setKrwAmount(BigDecimal krwAmount) { this.krwAmount = krwAmount; }

    public BigDecimal getAppliedRate() { return appliedRate; }
    public void setAppliedRate(BigDecimal appliedRate) { this.appliedRate = appliedRate; }

    public String getExchCpltDt() { return exchCpltDt; }
    public void setExchCpltDt(String exchCpltDt) { this.exchCpltDt = exchCpltDt; }
}