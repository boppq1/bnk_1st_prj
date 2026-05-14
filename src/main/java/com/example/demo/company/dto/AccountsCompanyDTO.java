package com.example.demo.company.dto;

import lombok.Data;

@Data
public class AccountsCompanyDTO {
	private int account_id;
	private String account_pw;
	private String bank_name;
	private String account_no;
	private int company_id;
	private int balance;
	private String currency;
	private String account_status;
	private int limit_one_time;
	private Long limit_daily;
}
