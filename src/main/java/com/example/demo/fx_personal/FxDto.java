package com.example.demo.fx_personal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FxDto {
	
	private String result;       // 결과 코드 (1: 성공)
    private String cur_unit;     // 통화코드 (USD, JPY 등)
    private String cur_nm;       // 국가/통화명
    private String ttb;          // 전신환 살때 (TTB)
    private String tts;          // 전신환 팔때 (TTS)
    private String deal_bas_r;   // 매매 기준율
    private String bkpr;         // 장부가격
    private String yy_efee_r;    // 연아자율
    private String ten_dd_efee_r; // 10일물 환가료율
    private String kftc_bkpr;    // 서울외국환중개 장부가격
    private String kftc_deal_bas_r; // 서울외국환중개 매매기준율

}
