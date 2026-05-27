package com.example.demo.fx_personal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExchangeService {

    private final ExchangeMapper mapper;

    public ExchangeService(ExchangeMapper mapper) {
        this.mapper = mapper;
    }

    private static final String ROLE = "ROLE_PERSONAL";
    private static final String KRW = "KRW";
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    /**
     * 통화 코드의 단위 배수를 반환.
     * JPY(100), IDR(100) 처럼 100단위로 고시되는 통화는 100을 반환,
     * 그 외에는 1을 반환한다.
     */
    private BigDecimal unitOf(String curCd) {
        if (curCd != null && curCd.contains("(100)")) {
            return new BigDecimal("100");
        }
        return BigDecimal.ONE;
    }

    /** 로그인 사용자의 전체 통화 최신 환율 목록 (프론트 로딩용) */
    public List<ExchangeRateDto> getLatestRates() {
        return mapper.findLatestRates();
    }

    /** LOGIN_ID 로 USR_NO 조회 */
    public Long getUsrNo(String loginId) {
        return mapper.findUsrNoByLoginId(loginId);
    }

    /**
     * 환전 처리 (매수/매도 공용).
     * 하나의 트랜잭션으로 잔액 검증 → 차감/증가 → 이력 기록까지 처리.
     */
    @Transactional
    public ExchangeResultDto exchange(Long usrNo, ExchangeRequestDto req) {

        // 0) 입력값 검증
        if (usrNo == null) {
            return ExchangeResultDto.fail("로그인 정보가 없습니다. 다시 로그인해 주세요.");
        }
        if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ExchangeResultDto.fail("환전 금액을 올바르게 입력해 주세요.");
        }
        String curCd = req.getCurrency();
        String dealType = req.getDealType();

        // 1) 환율 조회 (가장 최근 고시 기준)
        ExchangeRateDto rate = mapper.findLatestRateByCur(curCd);
        if (rate == null) {
            return ExchangeResultDto.fail("환율 정보가 없습니다. 잠시 후 다시 시도해 주세요.");
        }

        // 2) 계좌 조회
        AccountDto krwAcc = mapper.findKrwAccount(usrNo);
        Long foreignAcntId = mapper.findForeignAcntId(usrNo);
        if (krwAcc == null || foreignAcntId == null) {
            return ExchangeResultDto.fail("계좌 정보를 찾을 수 없습니다.");
        }

        BigDecimal unit = unitOf(curCd);
        BigDecimal foreignAmt = req.getAmount();   // 외화 금액 (외화 기준 입력)

        if ("BUY".equals(dealType)) {
            return doBuy(krwAcc, foreignAcntId, curCd, rate, unit, foreignAmt);
        } else if ("SELL".equals(dealType)) {
            return doSell(krwAcc, foreignAcntId, curCd, rate, unit, foreignAmt);
        } else {
            return ExchangeResultDto.fail("잘못된 거래 유형입니다.");
        }
    }

    /**
     * 외화 매수 (원화 → 외화).
     * 매수환율(BUY_RATE / TTS) 적용. 원화에서 차감, 외화 잔액 증가.
     */
    private ExchangeResultDto doBuy(AccountDto krwAcc, Long foreignAcntId, String curCd,
                                    ExchangeRateDto rate, BigDecimal unit, BigDecimal foreignAmt) {

        BigDecimal appliedRate = rate.getBuyRate();
        // 필요 원화 = 외화금액 × (매수환율 / 단위)
        BigDecimal krwNeeded = foreignAmt
                .multiply(appliedRate)
                .divide(unit, 0, RoundingMode.HALF_UP);

        BigDecimal krwBalance = krwAcc.getKrwBalance();
        if (krwBalance == null) krwBalance = BigDecimal.ZERO;

        // 잔액 검증
        if (krwBalance.compareTo(krwNeeded) < 0) {
            return ExchangeResultDto.fail(
                    "원화 잔액이 부족합니다. (보유: " + krwBalance.toPlainString()
                    + " KRW / 필요: " + krwNeeded.toPlainString() + " KRW)");
        }

        // 원화 차감
        BigDecimal newKrw = krwBalance.subtract(krwNeeded);
        mapper.updateKrwBalance(krwAcc.getKrwAcntId(), newKrw);

        // 외화 현재 잔액 (가장 최근 row 기준) + 매수 금액
        BigDecimal curForeign = mapper.findLatestForeignBalance(foreignAcntId, curCd);
        if (curForeign == null) curForeign = BigDecimal.ZERO;
        BigDecimal newForeign = curForeign.add(foreignAmt);
        mapper.insertBalance(foreignAcntId, curCd, newForeign);

        // 이력 기록 (원화계좌 → 외화계좌)
        LocalDateTime now = LocalDateTime.now();
        ExchangeHistory h = new ExchangeHistory();
        h.setUsrType(ROLE);
        h.setFromAcnt(krwAcc.getKrwAcntId());
        h.setToAcnt(foreignAcntId);
        h.setFromCur(KRW);
        h.setToCur(curCd);
        h.setFromAmt(krwNeeded);
        h.setToAmt(foreignAmt);
        h.setApldRate(appliedRate);
        mapper.insertExchangeRequest(h);

        return buildResult("BUY", curCd, foreignAmt, krwNeeded, appliedRate, now);
    }

    /**
     * 외화 매도 (외화 → 원화).
     * 매도환율(SELL_RATE / TTB) 적용. 외화 차감, 원화 증가.
     */
    private ExchangeResultDto doSell(AccountDto krwAcc, Long foreignAcntId, String curCd,
                                     ExchangeRateDto rate, BigDecimal unit, BigDecimal foreignAmt) {

        BigDecimal appliedRate = rate.getSellRate();

        // 외화 현재 잔액 확인
        BigDecimal curForeign = mapper.findLatestForeignBalance(foreignAcntId, curCd);
        if (curForeign == null) curForeign = BigDecimal.ZERO;

        if (curForeign.compareTo(foreignAmt) < 0) {
            return ExchangeResultDto.fail(
                    curCd + " 잔액이 부족합니다. (보유: " + curForeign.toPlainString()
                    + " / 요청: " + foreignAmt.toPlainString() + ")");
        }

        // 받을 원화 = 외화금액 × (매도환율 / 단위)
        BigDecimal krwGain = foreignAmt
                .multiply(appliedRate)
                .divide(unit, 0, RoundingMode.HALF_UP);

        // 외화 차감 (새 row INSERT)
        BigDecimal newForeign = curForeign.subtract(foreignAmt);
        mapper.insertBalance(foreignAcntId, curCd, newForeign);

        // 원화 증가
        BigDecimal krwBalance = krwAcc.getKrwBalance();
        if (krwBalance == null) krwBalance = BigDecimal.ZERO;
        BigDecimal newKrw = krwBalance.add(krwGain);
        mapper.updateKrwBalance(krwAcc.getKrwAcntId(), newKrw);

        // 이력 기록 (외화계좌 → 원화계좌)
        LocalDateTime now = LocalDateTime.now();
        ExchangeHistory h = new ExchangeHistory();
        h.setUsrType(ROLE);
        h.setFromAcnt(foreignAcntId);
        h.setToAcnt(krwAcc.getKrwAcntId());
        h.setFromCur(curCd);
        h.setToCur(KRW);
        h.setFromAmt(foreignAmt);
        h.setToAmt(krwGain);
        h.setApldRate(appliedRate);
        mapper.insertExchangeRequest(h);

        return buildResult("SELL", curCd, foreignAmt, krwGain, appliedRate, now);
    }

    private ExchangeResultDto buildResult(String dealType, String curCd,
                                          BigDecimal foreignAmt, BigDecimal krwAmt,
                                          BigDecimal appliedRate, LocalDateTime now) {
        ExchangeResultDto r = new ExchangeResultDto();
        r.setSuccess(true);
        r.setMessage("BUY".equals(dealType) ? "환전(매수)이 완료되었습니다." : "환전(매도)이 완료되었습니다.");
        r.setDealType(dealType);
        r.setCurrency(curCd);
        r.setForeignAmount(foreignAmt);
        r.setKrwAmount(krwAmt);
        r.setAppliedRate(appliedRate);
        r.setExchCpltDt(now.format(FMT));
        return r;
    }
}