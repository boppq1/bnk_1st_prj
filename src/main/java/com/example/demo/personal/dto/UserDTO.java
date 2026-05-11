package com.example.demo.personal.dto;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.Getter;

@Data
public class UserDTO {

	private String login_id;
	private String password;
	private String name;
	private String phon;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String birth;
	private String email;
	private String gender;
	private String e_name;
	private String postal_code;
	private String address;
	private String address_detail;
}
