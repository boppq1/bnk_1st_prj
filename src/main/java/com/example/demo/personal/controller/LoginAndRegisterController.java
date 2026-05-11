package com.example.demo.personal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import com.example.demo.personal.dto.UserRegistDTO;

@Controller
public class LoginAndRegisterController {

	@GetMapping("/registerPage")
	public String 회원가입페이지() {
		return "personal/registerPage";
	}
	
	@PostMapping("/registerPro")
	public String 회원가입(UserRegistDTO dto) {
		System.out.println("회원가입 컨트롤러");
		System.out.println(dto.toString());
		return null;
	}
}
