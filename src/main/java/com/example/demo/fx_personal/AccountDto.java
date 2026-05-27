package com.example.demo.fx_personal;

import java.math.BigDecimal;

/**
 * 계좌/잔액 관련 조회 결과를 담는 DTO
 * - 원화계좌(ACCOUNTS_PERSONAL), 외화계좌(FOREIGN_ACCOUNTS_PERSONAL),
 *   외화잔고(BALANCES) 조회에 공용으로 사용
 */
public class AccountDto {

    private Long usrNo;            // USR_NO   회원 고유 번호
    private Long krwAcntId;        // 원화 계좌 ACNT_ID
    private Long foreignAcntId;    // 외화 계좌 ACNT_ID
    private BigDecimal krwBalance; // 원화 계좌 잔액 (BALANCE)
    private String currency;       // 통화 코드
    private BigDecimal foreignBalance; // 외화 통화별 현재 잔액

    public AccountDto() {}

    public Long getUsrNo() { return usrNo; }
    public void setUsrNo(Long usrNo) { this.usrNo = usrNo; }

    public Long getKrwAcntId() { return krwAcntId; }
    public void setKrwAcntId(Long krwAcntId) { this.krwAcntId = krwAcntId; }

    public Long getForeignAcntId() { return foreignAcntId; }
    public void setForeignAcntId(Long foreignAcntId) { this.foreignAcntId = foreignAcntId; }

    public BigDecimal getKrwBalance() { return krwBalance; }
    public void setKrwBalance(BigDecimal krwBalance) { this.krwBalance = krwBalance; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getForeignBalance() { return foreignBalance; }
    public void setForeignBalance(BigDecimal foreignBalance) { this.foreignBalance = foreignBalance; }
}