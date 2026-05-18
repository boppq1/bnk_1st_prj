package com.example.demo.company.dto;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompanyUserDTO {
	//외래키
	private Long company_user_id;
	private int company_id;
	private String login_id;
	private String password;
	private String name;
	private String phone;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String birth;
	private String email;
	private String status;
	private String gender;
	private String e_name;
	private String created_at;
	private String updated_at;
	private String last_lgn_dt;
	         
}
