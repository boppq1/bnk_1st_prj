package com.example.demo.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.admin.dto.AdminDto;
import com.example.demo.admin.dto.SearchLogDto;
import com.example.demo.admin.service.AdminService;
import com.example.demo.company.dto.CompanyUserDTO;
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
	
	// ========== 멤버 ==========
	
	@GetMapping("/memberList")
	public String memberListPage(Model m) {
		m.addAttribute("userList", as.getUserList());
		m.addAttribute("companyList", as.getCompanyUserList());
		return "admin/memberList";
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
		
		UserDTO existingUser = as.getUser(dto.getUser_id());
		
		if(dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
			dto.setPassword(existingUser.getPassword());
		} else {
			// 나중에 여기서 비번 암호화 해야함
		}
		
		as.updateUser(dto);
		return "redirect:/admin/updateMemberPage?user_id=" + dto.getUser_id() + "&result=true";
	}
	
	@GetMapping("/updateCompanyMemberPage")
	public String updateCompanyMemberPage(@RequestParam("company_user_id") Long company_user_id, Model m, @RequestParam(required=false, name="result") String result) {
		m.addAttribute("company", as.getCompanyUser(company_user_id));
		if(result != null) {
			m.addAttribute("result", result);
			System.out.println("있다");
		}
		return "admin/updateCompanyMember";
	}
	
	@PostMapping("/updateCompanyMember")
	public String updateCompanyMember(CompanyUserDTO dto, Model m) {
		CompanyUserDTO existingUser = as.getCompanyUser(dto.getCompany_user_id());
		if(dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
			dto.setPassword(existingUser.getPassword());
		} else {
			// 나중에 여기서 비번 암호화 해야함
		}
		as.updateCompanyUser(dto);
		return "redirect:/admin/updateCompanyMemberPage?company_user_id=" + dto.getCompany_user_id() + "&result=true";
	}
	
	// ========== 관리자 승인 ==========
	
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
		
		AdminDto existingUser = as.getAdmin(dto.getAdmin_id());
		if(dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
			dto.setPassword(existingUser.getPassword());
		} else {
			// 나중에 여기서 비번 암호화 해야함
		}
		
		if(dto.getAdmin_pw() == null || dto.getAdmin_pw().trim().isEmpty()) {
			dto.setAdmin_pw(existingUser.getAdmin_pw());
		} else {
			// 나중에 여기서 비번 암호화 해야함
		}
		
		as.updateAdmin(dto);
		return "redirect:/admin/updateAdminPage?admin_id=" + dto.getAdmin_id() + "&result=true";
	}
	
	
	
	// ========== 상품 승인 ========== 
	
	@GetMapping("/approvalPage")
	public String approvalPage(Model m) {
		m.addAttribute("approvalList", as.getApprovals());
		return "admin/approvalPage";
	}
	
	@GetMapping("/approvalDetailPage")
	public String approvalDetailPage(Model m, @RequestParam("product_id") Long product_id) {
		m.addAttribute("approvalList", as.getApproval(product_id));
		return "admin/approvalDetailPage";
	}
	
	@GetMapping("/approved")
	public String approved(Model m, @RequestParam("product_id") Long product_id) {
		as.approvedStatus(product_id, "승인");
		return "redirect:/admin/approvalPage";
	}
	
	@GetMapping("/rejected")
	public String rejected(Model m, @RequestParam("product_id") Long product_id) {
		as.approvedStatus(product_id, "반려");
		return "redirect:/admin/approvalPage";
	}
	
	// ========== 로그 관련 ==========
	@GetMapping("/adminLogPage")
	public String adminLogPage(Model m) {
		m.addAttribute("adminLogList", as.adminLog());
		m.addAttribute("userLogList", as.userLog());
		return "/admin/adminLog";
	}
	
	// ========== 환전 내역 관련 ==========
	@GetMapping("/exchangeListPage")
	public String exchangeListPage(Model m) {
		m.addAttribute("exchangeList", as.exchangeList());
		System.out.println(as.exchangeList().getFirst());
		return "/admin/exchangeList";
	}
	
	// ========== 계좌 리스트 관련 ==========
	@GetMapping("/adminAccountPage")
	public String adminAccountPage(Model m) {
		m.addAttribute("user", as.getUserList());
		m.addAttribute("company",as.getCompanies());
		return "/admin/adminAccount";
	}
	
	@GetMapping("/getCompanyAccounts")
	public String getCompanyAccounts(Model m, @RequestParam("company_id") Long company_id) {
		m.addAttribute("domestic", as.getCompanyDomesticAccount(company_id));
		m.addAttribute("foreign", as.getCompanyForeignAccount(company_id));
		return "/admin/companyAccount";
	}
	
	@GetMapping("/getPersonalAccounts")
	public String getPersonalAccounts(Model m, @RequestParam("user_id") Long user_id) {
		m.addAttribute("domestic", as.getPersonalDomesticAccount(user_id));
		m.addAttribute("foreign", as.getPersonalForeignAccount(user_id));
		return "/admin/personalAccount";
	}
	
	// ========== 검색어 관리 ==========
	@GetMapping("/searchManagementPage")
	public String searchManagementPage(Model m) {
		m.addAttribute("search_per", as.getSearchPersonal());
		m.addAttribute("suggest_per", as.getSuggestPersonal());
		m.addAttribute("search_com", as.getSearchCompany());
		m.addAttribute("suggest_com", as.getSuggestCompany());
		m.addAttribute("ban", as.getKeywordBanList());
		return "/admin/searchManagement";
	}
	
	@GetMapping("/keywordBan")
	public String keywordBan(@RequestParam("keyword") String keyword) {
		if(as.checkKeywordExist(keyword)) {
			as.keywordBan(keyword, (long) 1); // adm_no 으로 세션에서 받아서 바꿔야함
			return "redirect:/admin/searchManagementPage";
		} else {
			return "redirect:/admin/searchManagementPage";
		}
	}
	
	@GetMapping("/updateSuggestKeyword")
	public String updateSuggestKeyword() {
		as.updateSuggestKeyword();
		return "redirect:/admin/searchManagementPage";
	}
	
	@GetMapping("/deleteBanKeyword")
	public String deleteBanKeyword(@RequestParam("keyword") String keyword) {
		as.deleteBanKeyword(keyword);
		return "redirect:/admin/searchManagementPage";
	}
	
	@GetMapping("/setSuggestPersonalKeyword")
	public String setSuggestPersonalKeyword(@RequestParam("keyword") String keyword) {
		
		// 동일한 키워드가 이미 있는지 확인
		if(as.getPersonalSuggestKeyword(keyword) == null) {
			
			// 추천 검색어 개수가 5개보다 크면 하나 제거
			if(as.getPersonalSuggestKeyword() >= 5) {
				as.deletePersonalSuggestKeyword();
			}
			
			SearchLogDto dto = as.getPersonalSearchLog(keyword);
			Long search_volume;
			if(dto == null) {
				search_volume = 0l;
			} else {
				search_volume = (long)Integer.parseInt(dto.getSearch_volume());
			}
			as.setSuggestKeyword(keyword, "ROLE_PERSONAL", search_volume);
			return "redirect:/admin/searchManagementPage";
		} else {
			return "redirect:/admin/searchManagementPage";
		}
		
	}
	
	@GetMapping("/setSuggestCompanyKeyword")
	public String setSuggestCompanyKeyword(@RequestParam("keyword") String keyword) {
		
		if(as.getCompanySuggestKeyword(keyword) == null) {
			
			// 추천 검색어 개수가 5개보다 크면 하나 제거
			if(as.getCompanySuggestKeyword() >= 5) {
				as.deleteCompanySuggestKeyword();
			}
			
			SearchLogDto dto = as.getCompanySearchLog(keyword);
			Long search_volume;
			if(dto == null) {
				search_volume = 0l;
			} else {
				search_volume = (long)Integer.parseInt(dto.getSearch_volume());
			}
			as.setSuggestKeyword(keyword, "ROLE_COMPANY", search_volume);
			return "redirect:/admin/searchManagementPage";
		} else {
			return "redirect:/admin/searchManagementPage";
		}
	}
	
}
