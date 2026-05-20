package com.example.demo.fx_personal;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FxDataDto {
	
	private int rate_no;
	private String cur_nm;
	private Double ttb;
	private Double tts;
	private Double deal_bas_r;
	private Date ntc_dt;

}
