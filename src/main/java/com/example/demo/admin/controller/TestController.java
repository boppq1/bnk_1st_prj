package com.example.demo.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
	@GetMapping("/mm")
	public String index() {
		return "index";
	}
	
	@GetMapping("/footer")
	public String footer() {
		return "footer";
	}
}
