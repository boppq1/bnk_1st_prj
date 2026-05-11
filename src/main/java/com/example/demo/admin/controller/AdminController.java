package com.example.demo.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	
	final AdminService as;
	
	@GetMapping("/adminMain")
	public String adminMain() {
		return "admin/adminMain";
	}
	
	@GetMapping("/memberList")
	public String memberListPage(Model m) {
		m.addAttribute("userList", as.getUserList());
		return "admin/memberList";
	}
	
	@GetMapping("/member")
	public String member(Long user_id, Model m) {
		m.addAttribute("user", as.getUser(user_id));
		return "admin/memberDetail";
	}
	
	@GetMapping("/adminList")
	public String adminListPage(Model m) {
		m.addAttribute("adminList", as.getAdminList());
		return "admin/adminList";
	}
	
	@GetMapping("/admin")
	public String admin(Long admin_id, Model m) {
		m.addAttribute("admin", as.getAdmin(admin_id));
		return "admin/adminDetail";
	}
	
}
