package com.example.demo.fx_personal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FxService {
	
	@Autowired
	FxDataDao fxDao;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String auth_key = 
    		"Eg9Yl9fEBQhpVXX1eyvo0QhAyHFM0Xiw";
    		//"VmNGokoNNC2jjX7I7WhAsGI5a5YUnnYk";

    public String getData(String searchdate) {

        String url = "https://oapi.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + auth_key + "&searchdate=" + searchdate + "&data=AP01";

        return restTemplate.getForObject(url, String.class);
    }
    
    public List<FxDataDto> getFxData(String cur_nm) {
    	
    	List<FxDataDto> fxData = fxDao.viewData(cur_nm);
    	
    	return fxData;
    }
    
    
    /**
     * 환율 조회 + 우대율 계산
     *
     * @param curNm    통화코드 (USD, JPY(100) ...)
     * @param date     기준일 "2025-05-22"
     * @param buySell  "buy" = 살 때 / "sell" = 팔 때
     * @param prefer   우대율 0~90 (%)
     */
    public ReturnCalculatorDto getRate(String curNm, String date,
                                   String buySell, int prefer) {

        // 1) DB 조회
    	CalculatorDto cDto = fxDao.selectRate(curNm, date);

        if (cDto == null) throw new RuntimeException("환율 데이터 없음: " + curNm);

        // 2) 살때/팔때 고시환율 선택
        //    buy(살때) → tts(은행이 파는 값, 고객이 더 비싸게 삼)
        //    sell(팔때) → ttb(은행이 사는 값, 고객이 더 싸게 팖)
        double noticeRate = buySell.equals("buy") ? cDto.getTts() : cDto.getTtb();
        double dealBasR   = cDto.getDealBasR();

        // 3) 스프레드 계산
        //    살때: 고시환율 - 매매기준율 (양수)
        //    팔때: 매매기준율 - 고시환율 (양수로 맞춤)
        double spread = Math.abs(noticeRate - dealBasR);

        // 4) 우대 절감액 = 스프레드 × 우대율
        double preferDiscount = spread * (prefer / 100.0);

        // 5) 적용환율
        //    살때: 고시환율 - 절감액 (싸게)
        //    팔때: 고시환율 + 절감액 (비싸게)
        double appliedRate = buySell.equals("buy")
                ? noticeRate - preferDiscount
                : noticeRate + preferDiscount;

        // 6) DTO 조립
        ReturnCalculatorDto dto = new ReturnCalculatorDto();
        dto.setCurNm(curNm);
        dto.setBaseDate(cDto.getNtcDt().toString());
        dto.setDealBasR(dealBasR);
        dto.setNoticeRate(noticeRate);
        dto.setSpread(spread);
        dto.setPreferDiscount(Math.round(preferDiscount * 100.0) / 100.0);
        dto.setAppliedRate(Math.round(appliedRate * 100.0) / 100.0);

        return dto;
    }

}