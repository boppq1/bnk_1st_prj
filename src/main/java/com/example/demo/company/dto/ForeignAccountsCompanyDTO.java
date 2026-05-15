package com.example.demo.company.dto;

import lombok.Data;

@Data
public class ForeignAccountsCompanyDTO {
	private Long account_id;
	private String account_pw;
	private String bank_name;
	private String account_no;
	//외래키
	private Long company_id;
	private String account_status;
	private Long limit_one_time;
	private Long limit_daily;
}
