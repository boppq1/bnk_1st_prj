package com.example.demo.company.dto;

import lombok.Data;

@Data
public class ForeginAccountsCompanyDTO {
	private int account_id;
	private String account_pw;
	private String bank_name;
	private String account_no;
	//외래키
	private int company_id;
	private String account_status;
	private int limit_one_time;
	private Long limit_daily;
}
