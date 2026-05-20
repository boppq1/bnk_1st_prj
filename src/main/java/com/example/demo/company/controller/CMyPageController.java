package com.example.demo.company.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CMyPageController {
	@GetMapping("/company/companyMyPage")
	public String 기업마이페이지() {
		return "company/companyMyPage";
	}
}
