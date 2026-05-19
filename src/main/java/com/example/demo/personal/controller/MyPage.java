package com.example.demo.personal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPage {

	@GetMapping("/personal/myPage")
	public String 마이페이지() {
		return "personal/myPage";
	}
}
