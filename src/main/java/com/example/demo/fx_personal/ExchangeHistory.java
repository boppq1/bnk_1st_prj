package com.example.demo.fx_personal;

import java.math.BigDecimal;

/**
 * EXCHANGE_REQUESTS 테이블 INSERT 용 DTO
 */
public class ExchangeHistory {

    private String usrType;     // USR_TYPE   회원 유형 (ROLE_PERSONAL)
    private Long fromAcnt;      // FROM_ACNT  출금 계좌 ACNT_ID
    private Long toAcnt;        // TO_ACNT    입금 계좌 ACNT_ID
    private String fromCur;     // FROM_CUR   출금 통화
    private String toCur;       // TO_CUR     입금 통화
    private BigDecimal fromAmt; // FROM_AMT   출금 금액
    private BigDecimal toAmt;   // TO_AMT     입금 금액
    private BigDecimal apldRate;// APLD_RATE  적용 환율

    public ExchangeHistory() {}

    public String getUsrType() { return usrType; }
    public void setUsrType(String usrType) { this.usrType = usrType; }

    public Long getFromAcnt() { return fromAcnt; }
    public void setFromAcnt(Long fromAcnt) { this.fromAcnt = fromAcnt; }

    public Long getToAcnt() { return toAcnt; }
    public void setToAcnt(Long toAcnt) { this.toAcnt = toAcnt; }

    public String getFromCur() { return fromCur; }
    public void setFromCur(String fromCur) { this.fromCur = fromCur; }

    public String getToCur() { return toCur; }
    public void setToCur(String toCur) { this.toCur = toCur; }

    public BigDecimal getFromAmt() { return fromAmt; }
    public void setFromAmt(BigDecimal fromAmt) { this.fromAmt = fromAmt; }

    public BigDecimal getToAmt() { return toAmt; }
    public void setToAmt(BigDecimal toAmt) { this.toAmt = toAmt; }

    public BigDecimal getApldRate() { return apldRate; }
    public void setApldRate(BigDecimal apldRate) { this.apldRate = apldRate; }
}