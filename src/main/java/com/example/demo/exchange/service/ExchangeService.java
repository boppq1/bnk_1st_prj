package com.example.demo.exchange.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exchange.dao.ExchangeDao;
import com.example.demo.exchange.dto.response.ExchangeRateResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExchangeService {

    private final ExchangeDao exchangeDao;

    public ExchangeRateResponseDto getLatestRate(String currencyCode) {

        if (currencyCode == null || currencyCode.isBlank()) {
            throw new IllegalArgumentException("통화코드가 필요합니다.");
        }

        String dbCurrencyCode = convertToDbCurrencyCode(currencyCode);

        ExchangeRateResponseDto rate =
                exchangeDao.selectLatestRateByCurrencyCode(dbCurrencyCode);

        if (rate == null) {
            throw new IllegalArgumentException(
                    "해당 통화의 환율 정보를 찾을 수 없습니다. currencyCode=" + currencyCode
            );
        }

        return rate;
    }

    private String convertToDbCurrencyCode(String currencyCode) {

        if ("JPY".equals(currencyCode)) {
            return "JPY(100)";
        }

        if ("IDR".equals(currencyCode)) {
            return "IDR(100)";
        }

       
        if ("CNY".equals(currencyCode)) {
            return "CNH";
        }

        return currencyCode;
    }
}