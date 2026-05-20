package com.example.demo.personal.dto;

import lombok.Data;

@Data
public class MyPageAcntDTO {

	private long acnt_no;
	private String secu_acnt_pwd;
	private String bank_nm;
	private String acnt_num;
	private int usr_no;
	private int balance;
	private String currency;
	private String acnt_stat_cd;
	private int lmt_once;
	private int lmt_daily;
}
