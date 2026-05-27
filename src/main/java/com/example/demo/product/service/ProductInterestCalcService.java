package com.example.demo.product.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exchange.dto.response.ExchangeRateResponseDto;
import com.example.demo.exchange.service.ExchangeService;
import com.example.demo.product.dto.request.ForeignInterestCalcRequestDto;
import com.example.demo.product.dto.response.InterestCalcResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductInterestCalcService {

    private final ExchangeService exchangeService;

    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal TWELVE = new BigDecimal("12");
    private static final BigDecimal AFTER_TAX_RATE = new BigDecimal("0.846");

    public InterestCalcResponseDto calculateForeign(ForeignInterestCalcRequestDto dto) {

        ExchangeRateResponseDto exchangeRate =
                exchangeService.getLatestRate(dto.getCurrencyCode());

        BigDecimal krwAmount = BigDecimal.valueOf(dto.getKrwAmount());
        BigDecimal productRate = dto.getRate();

        BigDecimal periodYear = BigDecimal.valueOf(dto.getPeriodMonth())
                .divide(TWELVE, 10, RoundingMode.HALF_UP);

        BigDecimal tts = exchangeRate.getTts();

        BigDecimal expectedRate = dto.getExpectedRate() != null
                ? dto.getExpectedRate()
                : exchangeRate.getTtb();

        BigDecimal currencyUnit = BigDecimal.valueOf(exchangeRate.getCurrencyUnit());

        /*
         * 원화 → 외화 환산
         *
         * USD:
         * 1,000,000 / 1,497.72 * 1
         *
         * JPY(100):
         * 1,000,000 / 938.92 * 100
         */
        BigDecimal foreignPrincipal = krwAmount
                .divide(tts, 10, RoundingMode.HALF_UP)
                .multiply(currencyUnit);

        BigDecimal beforeTaxInterest = foreignPrincipal
                .multiply(productRate.divide(HUNDRED, 10, RoundingMode.HALF_UP))
                .multiply(periodYear);

        BigDecimal afterTaxInterest = beforeTaxInterest.multiply(AFTER_TAX_RATE);

        BigDecimal maturityForeignAmount = foreignPrincipal.add(afterTaxInterest);

        /*
         * 외화 → 원화 환산
         *
         * USD:
         * 외화금액 * 환율 / 1
         *
         * JPY(100):
         * 외화금액 * 환율 / 100
         */
        BigDecimal maturityKrwAmount = maturityForeignAmount
                .multiply(expectedRate)
                .divide(currencyUnit, 0, RoundingMode.HALF_UP);

        return InterestCalcResponseDto.builder()
                .calcType("FOREIGN")
                .currencyCode(exchangeRate.getDisplayCurrencyCode())
                .inputKrwAmount(dto.getKrwAmount())
                .appliedRate(productRate)
                .periodMonth(dto.getPeriodMonth())
                .exchangeRate(tts)
                .foreignPrincipal(foreignPrincipal.setScale(2, RoundingMode.HALF_UP))
                .beforeTaxInterest(beforeTaxInterest.setScale(2, RoundingMode.HALF_UP))
                .afterTaxInterest(afterTaxInterest.setScale(2, RoundingMode.HALF_UP))
                .maturityForeignAmount(maturityForeignAmount.setScale(2, RoundingMode.HALF_UP))
                .maturityKrwAmount(maturityKrwAmount)
                .message("※현재 고시 환율 기준 예상 금액입니다. 실제 원화 수령액은 만기 시 환율 변동 및 환전수수료에 따라 달라질 수 있습니다.")
                .build();
    }
}