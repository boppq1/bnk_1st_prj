package com.example.demo.fx_personal;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExchangeMapper {

    /* ── 사용자 / 계좌 조회 ── */

    /** LOGIN_ID 로 USR_NO 조회 */
    Long findUsrNoByLoginId(@Param("loginId") String loginId);

    /** USR_NO 로 원화 개인계좌 (ACNT_ID + BALANCE) 조회 */
    AccountDto findKrwAccount(@Param("usrNo") Long usrNo);

    /** USR_NO 로 개인 외화계좌 ACNT_ID 조회 */
    Long findForeignAcntId(@Param("usrNo") Long usrNo);

    /* ── 환율 조회 ── */

    /**
     * 전체 통화의 "가장 최근 고시일" 환율 목록 조회.
     * 주말/공휴일이라 오늘자가 없어도 가장 최근 영업일 환율을 가져온다.
     */
    List<ExchangeRateDto> findLatestRates();

    /** 특정 통화의 가장 최근 환율 1건 조회 (환전 처리 시 단가 확정용) */
    ExchangeRateDto findLatestRateByCur(@Param("curCd") String curCd);

    /* ── 잔액 조회 (BALANCES) ── */

    /**
     * 외화계좌 + 통화 기준, 가장 최근 row 1건의 현재 잔액 조회.
     * INSERT only 구조이므로 UPD_DT 최신 row 가 곧 현재 잔액.
     * 거래 이력이 전혀 없으면 null 반환.
     */
    BigDecimal findLatestForeignBalance(@Param("acntId") Long acntId,
                                        @Param("curCd") String curCd);

    /* ── 잔액 갱신 ── */

    /** 원화 계좌 잔액 갱신 (절대값으로 set) */
    int updateKrwBalance(@Param("acntId") Long acntId,
                         @Param("balance") BigDecimal balance);

    /** BALANCES 에 새 잔액 row INSERT (수정시각 SYSDATE) */
    int insertBalance(@Param("acntId") Long acntId,
                      @Param("curCd") String curCd,
                      @Param("balance") BigDecimal balance);

    /* ── 환전 이력 ── */

    /** EXCHANGE_REQUESTS 에 환전 내역 INSERT */
    int insertExchangeRequest(ExchangeHistory history);
}