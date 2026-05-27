package com.example.demo.fx_personal;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class ReturnCalculatorDto {
	
	private String curNm;           // "USD"
    private String baseDate;        // "2025-05-22"
    private double dealBasR;        // 매매기준율   1503.50
    private double noticeRate;      // 고시환율     1518.53 (살때)
    private double spread;          // 스프레드       15.03
    private double preferDiscount;  // 우대 절감액     7.52  (50% 적용 시)
    private double appliedRate;     // 적용환율     1511.01

}
