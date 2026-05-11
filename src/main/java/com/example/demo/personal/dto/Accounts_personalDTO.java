package com.example.demo.personal.dto;

import lombok.Data;

@Data
public class Accounts_personalDTO {

	private int account_pw;
	private String bank_name;
	private String account_no;
	private String user_id;
	private int balance;
	private String currency;
	private String account_status;
	private int limit_one_time;
	private int limit_daily;
}
