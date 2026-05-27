package com.example.demo.fx_personal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * EXCHANGE_RATES 테이블 매핑 DTO
 * 통화별 매수/매도/기준 환율 정보
 */
public class ExchangeRateDto {

    private Long rateNo;          // RATE_NO  환율 고유 번호
    private String curCd;         // CUR_CD   통화 코드 (USD, JPY(100) 등)
    private BigDecimal buyRate;   // BUY_RATE 매수 환율 (TTS, 고객이 외화 살 때)
    private BigDecimal sellRate;  // SELL_RATE 매도 환율 (TTB, 고객이 외화 팔 때)
    private BigDecimal baseRate;  // BASE_RATE 기준 환율
    private LocalDateTime ntcDt;  // NTC_DT   고시 시각

    public ExchangeRateDto() {}

    public Long getRateNo() { return rateNo; }
    public void setRateNo(Long rateNo) { this.rateNo = rateNo; }

    public String getCurCd() { return curCd; }
    public void setCurCd(String curCd) { this.curCd = curCd; }

    public BigDecimal getBuyRate() { return buyRate; }
    public void setBuyRate(BigDecimal buyRate) { this.buyRate = buyRate; }

    public BigDecimal getSellRate() { return sellRate; }
    public void setSellRate(BigDecimal sellRate) { this.sellRate = sellRate; }

    public BigDecimal getBaseRate() { return baseRate; }
    public void setBaseRate(BigDecimal baseRate) { this.baseRate = baseRate; }

    public LocalDateTime getNtcDt() { return ntcDt; }
    public void setNtcDt(LocalDateTime ntcDt) { this.ntcDt = ntcDt; }
}