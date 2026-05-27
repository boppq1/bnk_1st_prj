package com.example.demo.exchange.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.exchange.dto.response.ExchangeRateResponseDto;

@Mapper
public interface ExchangeDao {

    /*
     * 통화코드로 가장 최신 환율 1건 조회
     *
     * 예:
     * USD 입력
     * → EXCHANGE_RATES 테이블에서 USD 최신 환율 조회
     */
    ExchangeRateResponseDto selectLatestRateByCurrencyCode(
            @Param("currencyCode") String currencyCode
    );
}