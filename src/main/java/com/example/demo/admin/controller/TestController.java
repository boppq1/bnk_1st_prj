package com.example.demo.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
	@GetMapping("/index")
	public String 인덱스() {
	    return "index";
	}
	
	@GetMapping("/footer")
	public String footer() {
		return "footer";
	}
}
