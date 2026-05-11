package com.example.demo.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.admin.dto.AdminDto;
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

	@GetMapping("/adminList")
	public String adminListPage(Model m) {
		m.addAttribute("adminList", as.getAdminList());
		return "admin/adminList";
	}
	
	@GetMapping("/updateAdminPage")
	public String updateAdmin(@RequestParam("admin_id") Long admin_id, Model m) {
		System.out.println(">>>>>>" + admin_id);
		m.addAttribute("admin", as.updateAdmin(admin_id));
		return "admin/updateAdmin";
	}
	
	@GetMapping("/updateMemberPage")
	public String updateMember(@RequestParam("user_id") Long user_id, Model m) {
		System.out.println(">>>>>>" + user_id);
		m.addAttribute("user", as.updateUser(user_id));
		return "admin/updateMember";
	}
	
	@GetMapping("/approvalPage")
	public String approvalPage(Model m) {
		m.addAttribute("approvalList", as.getApprovals());
		return "admin/approvalPage";
	}
	
	@GetMapping("/approvalDetailPage")
	public String approvalDetailPage(Model m, @RequestParam("product_id") Long product_id) {
		m.addAttribute("approvalList", as.getProduct(product_id));
		System.out.println(">>>>> " + as.getProduct(product_id));
		return "admin/approvalDetailPage";
	}
	
	@GetMapping("/approved")
	public String approved(Model m, @RequestParam("product_id") Long product_id) {
		as.approvedStatus(product_id, "APPROVED");
		return "redirect:/admin/approvalPage";
	}
	
	@GetMapping("/pending")
	public String pending(Model m, @RequestParam("product_id") Long product_id) {
		as.approvedStatus(product_id, "PENDING");
		return "redirect:/admin/approvalPage";
	}
	
	@GetMapping("/rejected")
	public String rejected(Model m, @RequestParam("product_id") Long product_id) {
		as.approvedStatus(product_id, "REJECTED");
		return "redirect:/admin/approvalPage";
	}
	
	
}
