package com.example.demo.personal.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MyPageUserDTO {
	// 회원테이블
	private Long usr_no;
	private String login_id;
	private String secu_pw;
	private String usr_nm;
	private String tel_no;
	private String birth_dt;
	private String email;
	private String acnt_stat_cd;
	private String gender;
	private String eng_nm;
	private String zip_cd;
	private String addr;
	private String addr_dtl;
	private String reg_dt;
	private String upd_dt;
	private String last_lgn_dt;


}
