package com.example.demo.fx_personal;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculatorDto {

	private Long rateNo;
	private String curNm; // "USD", "JPY(100)"
	private Double ttb; // 살 때
	private Double tts; // 팔 때
	private Double dealBasR; // 매매기준율
	private LocalDate ntcDt;

}
