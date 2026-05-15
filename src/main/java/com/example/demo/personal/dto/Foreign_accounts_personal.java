package com.example.demo.personal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Foreign_accounts_personal {

	private Long account_id;
	private String account_pw;
	private String bank_name;
	private String account_no;
	private int user_id;
	private String account_status;
	private Long limit_one_time;
	private Long limit_daily;
	
}
