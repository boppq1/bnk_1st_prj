package com.example.demo.personal.dto;

import lombok.Data;

@Data
public class Accounts_personalDTO {

	private Long account_id;
	private String account_pw;
	private String bank_name;
	private String account_no;
	private Long user_id;
	private Long balance;
	private String currency;
	private String account_status;
	private Long limit_one_time;
	private Long limit_daily;
}
