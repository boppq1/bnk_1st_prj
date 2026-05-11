package com.example.demo.personal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import com.example.demo.personal.dto.UserRegistDTO;
import com.example.demo.personal.service.LoginAndRegisterService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Controller
public class LoginAndRegisterController {
	
	private final LoginAndRegisterService registerService;
	
	@GetMapping("/registerPage")
	public String 회원가입페이지() {
		return "personal/registerPage";
	}
	
	@PostMapping("/registerPro")
	public String 회원가입(UserRegistDTO dto) {
		System.out.println("회원가입 컨트롤러");
		System.out.println(dto.toString());
		if(registerService.register(dto) == 1) {
			System.out.println("성공");
			return "redirect:/index";
		}
		return "redirect:/registerPage";
	}
}
