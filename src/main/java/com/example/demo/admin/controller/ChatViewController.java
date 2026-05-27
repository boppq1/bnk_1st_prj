package com.example.demo.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatViewController {
	
	@GetMapping("/chatPage")
	public String chatPage() {
		return "/common/chatPage";
	}
}
