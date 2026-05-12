package com.example.demo.personal.dto;

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
public class UserDTO {

	private Long user_id;
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
	private String postal_code;
	private String address;
	private String address_detail;
}
