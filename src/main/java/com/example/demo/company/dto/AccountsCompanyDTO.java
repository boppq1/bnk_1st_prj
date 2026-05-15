package com.example.demo.company.dto;

import lombok.Data;

@Data
public class AccountsCompanyDTO {
	private Long account_id;
	private String account_pw;
	private String bank_name;
	private String account_no;
	private Long company_id;
	private Long balance;
	private String currency;
	private String account_status;
	private Long limit_one_time;
	private Long limit_daily;
}
