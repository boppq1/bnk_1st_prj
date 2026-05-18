package com.example.demo.commonLogin.dto;

import lombok.Data;

@Data
public class UserLoginDTO {

	private String login_id;
	private String secu_pw;
	private String usr_nm;
	private String role;
}
