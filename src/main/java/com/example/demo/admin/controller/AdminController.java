package com.example.demo.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.admin.dto.AdminDto;
import com.example.demo.admin.service.AdminService;
import com.example.demo.personal.dto.UserDTO;

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
	public String updateAdminPage(@RequestParam("admin_id") Long admin_id, Model m, @RequestParam(required=false, name="result") String result) {
		m.addAttribute("admin", as.getAdmin(admin_id));
		if(result != null) {
			m.addAttribute("result", result);
			System.out.println("있다");
		}
		return "admin/updateAdmin";
	}
	
	@PostMapping("/updateAdmin")
	public String updateAdmin(AdminDto dto, Model m) {
		as.updateAdmin(dto);
		return "redirect:/admin/updateAdminPage?admin_id=" + dto.getAdmin_id() + "&result=true";
	}
	
	@GetMapping("/updateMemberPage")
	public String updateMemberPage(@RequestParam("user_id") Long user_id, Model m, @RequestParam(required=false, name="result") String result) {
		m.addAttribute("user", as.getUser(user_id));
		if(result != null) {
			m.addAttribute("result", result);
			System.out.println("있다");
		}
		return "admin/updateMember";
	}
	
	@PostMapping("/updateMember")
	public String updateMember(UserDTO dto, Model m) {
		as.updateUser(dto);
		return "redirect:/admin/updateMemberPage?user_id=" + dto.getUser_id() + "&result=true";
	}
	
	// ========== 승인 관련 ========== 
	
	@GetMapping("/approvalPage")
	public String approvalPage(Model m) {
		m.addAttribute("approvalList", as.getApprovals());
		return "admin/approvalPage";
	}
	
	@GetMapping("/approvalDetailPage")
	public String approvalDetailPage(Model m, @RequestParam("product_id") Long product_id) {
		m.addAttribute("approvalList", as.getProduct(product_id));
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
