package com.example.demo.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/error")
public class ErrorPageController {
	@GetMapping("/blacklist")
	public String blackListPage(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		return "error/blacklist";
	}
}
