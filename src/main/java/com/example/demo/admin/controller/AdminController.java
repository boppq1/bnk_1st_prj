package com.example.demo.admin.controller;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.admin.dto.AdminDto;
import com.example.demo.admin.dto.KeywordBanDto;
import com.example.demo.admin.dto.NewsDto;
import com.example.demo.admin.dto.SearchDto;
import com.example.demo.admin.service.AdminEventService;
import com.example.demo.admin.service.AdminMergeService;
import com.example.demo.admin.service.AdminService;
import com.example.demo.admin.service.BlacklistService;
import com.example.demo.company.dto.CompanyUserDTO;
import com.example.demo.interceptor.AdminLog;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.personal.dto.UserDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	
	final AdminService as;
	private final BlacklistService bs;
	private final JwtUtil jwt;
	private final AdminMergeService mergeServ;
	
	@GetMapping("/adminMain")
	public String adminMain() {
		return "admin/adminMain";
	}
	
	// ========== 멤버 ==========
	
	@GetMapping("/memberList")
	public String memberListPage(Model m, @CookieValue(value = "accessToken") String token) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("userList", as.getUserList());
		m.addAttribute("companyList", as.getCompanyUserList());
		System.out.println("");
		return "admin/memberList";
	}
	
	@GetMapping("/updateMemberPage")
	public String updateMemberPage(@CookieValue(value = "accessToken") String token, @RequestParam("user_id") Long user_id, Model m, @RequestParam(required=false, name="result") String result) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("user", as.getUser(user_id));
		if(result != null) {
			m.addAttribute("result", result);
		}
		return "admin/updateMember";
	}
	
	@AdminLog(action="개인 회원 수정")
	@PostMapping("/updateMember")
	public String updateMember(UserDTO dto, Model m) {
		
		UserDTO existingUser = as.getUser(dto.getUser_id());
		
		if(dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
			dto.setPassword(existingUser.getPassword());
		} else {
			dto.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
		}
		
		as.updateUser(dto);
		return "redirect:/admin/updateMemberPage?user_id=" + dto.getUser_id() + "&result=true";
	}
	
	@GetMapping("/updateCompanyMemberPage")
	public String updateCompanyMemberPage(@CookieValue(value = "accessToken") String token, @RequestParam("company_user_id") Long company_user_id, Model m, @RequestParam(required=false, name="result") String result) {
		m.addAttribute("company", as.getCompanyUser(company_user_id));
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		if(result != null) {
			m.addAttribute("result", result);
			System.out.println("있다");
		}
		return "admin/updateCompanyMember";
	}
	
	@AdminLog(action="기업 회원 수정")
	@PostMapping("/updateCompanyMember")
	public String updateCompanyMember(CompanyUserDTO dto, Model m) {
		CompanyUserDTO existingUser = as.getCompanyUser(dto.getCompany_user_id());
		if(dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
			dto.setPassword(existingUser.getPassword());
		} else {
			dto.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
		}
		as.updateCompanyUser(dto);
		return "redirect:/admin/updateCompanyMemberPage?company_user_id=" + dto.getCompany_user_id() + "&result=true";
	}
	
	// ========== 관리자 승인 ==========
	@GetMapping("/adminList")
	public String adminListPage(@CookieValue(value = "accessToken") String token, Model m) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("adminList", as.getAdminList());
		return "admin/adminList";
	}
	
	@GetMapping("/updateAdminPage")
	public String updateAdminPage(@CookieValue(value = "accessToken") String token, @RequestParam("admin_id") Long admin_id, Model m, @RequestParam(required=false, name="result") String result) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("admin", as.getAdmin(admin_id));
		if(result != null) {
			m.addAttribute("result", result);
		}
		return "admin/updateAdmin";
	}
	
	@AdminLog(action="관리자 회원 수정")
	@PostMapping("/updateAdmin")
	public String updateAdmin(AdminDto dto, Model m) {
		
		AdminDto existingUser = as.getAdmin(dto.getAdmin_id());
		if(dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
			dto.setPassword(existingUser.getPassword());
		} else {
			dto.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
		}
		
		if(dto.getAdmin_pw() == null || dto.getAdmin_pw().trim().isEmpty()) {
			dto.setAdmin_pw(existingUser.getAdmin_pw());
		} else {
			dto.setPassword(BCrypt.hashpw(dto.getAdmin_pw(), BCrypt.gensalt()));
		}
		
		as.updateAdmin(dto);
		return "redirect:/admin/updateAdminPage?admin_id=" + dto.getAdmin_id() + "&result=true";
	}
	
	// ========== 상품 승인 ========== 
	@GetMapping("/approvalPage")
	public String approvalPage(@CookieValue(value = "accessToken") String token, Model m) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("approvalList", as.getApprovals());
		return "admin/approvalPage";
	}
	
	@GetMapping("/approvalDetailPage")
	public String approvalDetailPage(@CookieValue(value = "accessToken") String token, Model m, @RequestParam("product_id") Long product_id) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("approvalList", as.getApproval(product_id));
		return "admin/approvalDetailPage";
	}
	
	@AdminLog(action="상품 승인")
	@GetMapping("/approved")
	public String approved(Model m, @RequestParam("product_id") Long product_id) {
		as.approvedStatus(product_id, "승인");
		return "redirect:/admin/approvalPage";
	}
	
	@AdminLog(action="상품 반려")
	@GetMapping("/rejected")
	public String rejected(Model m, @RequestParam("product_id") Long product_id, @RequestParam("rej_reason") String rej_reason) {
		as.rejectApprove(product_id, "반려", rej_reason);
		return "redirect:/admin/approvalPage";
	}
	
	// ========== 로그 관련 ==========
	@GetMapping("/adminLogPage")
	public String adminLogPage(@CookieValue(value = "accessToken") String token,  Model m) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("adminLogList", as.adminLog());
		m.addAttribute("userLogList", as.userLog());
		return "/admin/adminLog";
	}
	
	// ========== 환전 내역 관련 ==========
	@GetMapping("/exchangeListPage")
	public String exchangeListPage(@CookieValue(value = "accessToken") String token,  Model m) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("exchangeList", as.exchangeList());
		return "/admin/exchangeList";
	}
	
	// ========== 계좌 리스트 관련 ==========
	@GetMapping("/adminAccountPage")
	public String adminAccountPage(@CookieValue(value = "accessToken") String token, Model m) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("user", as.getUserList());
		System.out.println("크기 : " + as.getUserList().size());
		m.addAttribute("company", as.getCompanies());
		System.out.println("크기 : " + as.getCompanies().size());
		return "/admin/adminAccount";
	}
	
	@GetMapping("/getCompanyAccounts")
	public String getCompanyAccounts(@CookieValue(value = "accessToken") String token, Model m, @RequestParam("company_id") Long company_id) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("domestic", as.getCompanyDomesticAccount(company_id));
		m.addAttribute("foreign", as.getCompanyForeignAccount(company_id));
		return "/admin/companyAccount";
	}
	
	@GetMapping("/getPersonalAccounts")
	public String getPersonalAccounts(@CookieValue(value = "accessToken") String token, Model m, @RequestParam("user_id") Long user_id) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("domestic", as.getPersonalDomesticAccount(user_id));
		m.addAttribute("foreign", as.getPersonalForeignAccount(user_id));
		return "/admin/personalAccount";
	}
	
	// ========== 검색어 관리 ==========
	@GetMapping("/searchManagementPage")
	public String searchManagementPage(@CookieValue(value = "accessToken") String token, Model m, @RequestParam(value = "warning", required = false) String warning) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("warning", warning);
		m.addAttribute("search_per", as.getSearchPersonal());
		m.addAttribute("suggest_per", as.getSuggestPersonal());
		m.addAttribute("search_com", as.getSearchCompany());
		m.addAttribute("suggest_com", as.getSuggestCompany());
		m.addAttribute("ban", as.getKeywordBanList());
		return "/admin/searchManagement";
	}
	
	@AdminLog(action="검색어 차단")
	@GetMapping("/keywordBan")
	public String keywordBan(@RequestParam("keyword") String keyword, @CookieValue(value = "accessToken") String token) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		
		if(as.checkKeywordExist(keyword)) {
			as.keywordBan(keyword, dto.getAdmin_id());
			return "redirect:/admin/searchManagementPage";
		} else {
			return "redirect:/admin/searchManagementPage";
		}
	}
	
	@AdminLog(action="추천 검색어 목록 리프레쉬")
	@GetMapping("/updateSuggestKeyword")
	public String updateSuggestKeyword() {
		as.updateSuggestKeyword();
		return "redirect:/admin/searchManagementPage";
	}
	
	@AdminLog(action="차단 검색어 삭제")
	@GetMapping("/deleteBanKeyword")
	public String deleteBanKeyword(@RequestParam("keyword") String keyword) {
		as.deleteBanKeyword(keyword);
		return "redirect:/admin/searchManagementPage";
	}
	
	@AdminLog(action="개인 추천 검색어 추가")
	@GetMapping("/setSuggestPersonalKeyword")
	public String setSuggestPersonalKeyword(@RequestParam("keyword") String keyword) {
		List<KeywordBanDto> keywordBanList = as.getKeywordBanList();
		for(KeywordBanDto dto : keywordBanList) {
			if(dto.getKeyword().equals(keyword)) {
				return "redirect:/admin/searchManagementPage?warning=true";
			}
		}
		
		// 동일한 키워드가 이미 있는지 확인
		if(as.getPersonalSuggestKeyword(keyword) == null) {
			
			// 추천 검색어 개수가 5개보다 크면 하나 제거
			if(as.getPersonalSuggestKeyword() >= 5) {
				as.deletePersonalSuggestKeyword();
			}
			
			SearchDto dto = as.getPersonalSearchLog(keyword);
			
			as.setSuggestKeyword(keyword, "ROLE_PERSONAL", 0l);
			return "redirect:/admin/searchManagementPage";
		} else {
			return "redirect:/admin/searchManagementPage";
		}
		
	}
	
	@AdminLog(action="기업 추천 검색어 추가")
	@GetMapping("/setSuggestCompanyKeyword")
	public String setSuggestCompanyKeyword(@RequestParam("keyword") String keyword) {
		List<KeywordBanDto> keywordBanList = as.getKeywordBanList();
		for(KeywordBanDto dto : keywordBanList) {
			if(dto.getKeyword().equals(keyword)) {
				return "redirect:/admin/searchManagementPage?warning=true";
			}
		}
		
		if(as.getCompanySuggestKeyword(keyword) == null) {
			
			// 추천 검색어 개수가 5개보다 크면 하나 제거
			if(as.getCompanySuggestKeyword() >= 5) {
				as.deleteCompanySuggestKeyword();
			}
			
			
			as.setSuggestKeyword(keyword, "ROLE_COMPANY", 0L);
			return "redirect:/admin/searchManagementPage";
		} else {
			return "redirect:/admin/searchManagementPage";
		}
	}
	
	// ========== 챗봇 ==========
	@GetMapping("/chatPage")
	public String chatPage() {
		return "/common/chatPage";
	}
	
	// ========== 공지사항 ==========
	@GetMapping("/newsPage")
	public String newsPage(@CookieValue(value = "accessToken") String token, Model m) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("news", as.getNews());
		return "/admin/newsPage";
	}
	
	@GetMapping("/makeNewsPage")
	public String makeNewsPage(@CookieValue(value = "accessToken") String token, Model m) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		return "/admin/makeNewsPage";
	}
	
	@GetMapping("/getOneNews")
	public String getOneNewsPage(@CookieValue(value = "accessToken") String token, Model m, @RequestParam("news_no") Long news_no) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("news", as.getOneNews(news_no));
		return "/admin/getOneNewsPage";
	}
	
	@AdminLog(action="공지사항 생성")
	@PostMapping("/makeNews")
	public String makeNews(NewsDto dto, @CookieValue(value = "accessToken") String token) {
		String id = jwt.getLoginId(token);
        AdminDto admin = mergeServ.selectMyPage(id);
		
		dto.setNews_wtr_no(admin.getAdmin_id());
		as.makeNews(dto);
		return "redirect:/admin/newsPage";
	}
	
	@GetMapping("/updateNewsPage")
	public String updateNewsPage(@RequestParam("news_no") Long news_no, Model m, @CookieValue(value = "accessToken") String token) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("news", as.getOneNews(news_no));
		return "admin/updateNewsPage";
	}
	
	@AdminLog(action="공지사항 수정")
	@PostMapping("/updateNews")
	public String updateNews(NewsDto dto) {
		as.updateNews(dto);
		return "redirect:/admin/getOneNews?news_no=" + dto.getNews_no();
	}
	
	@AdminLog(action="공지사항 삭제")
	@GetMapping("/deleteNews")
	public String deleteNews(@RequestParam("news_no") Long news_no) {
		as.deleteNews(news_no);
		return "redirect:/admin/newsPage";
	}
	
	@GetMapping("/executiveDashboard")
	public String executiveDashboard(Model m, @CookieValue(value = "accessToken") String token) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("approval", as.outstandingNumber());
		m.addAttribute("adminLog", as.adminLogFive());
		m.addAttribute("userLog", as.userLogFive());
		m.addAttribute("black", bs.getBlacklistFive());
		return "/admin/executiveDashboard";
	}
	
	private final AdminEventService serv;
	
	@GetMapping("/chiefDashboard")
	public String chiefDashboard(Model m, @CookieValue(value = "accessToken") String token) {
		String id = jwt.getLoginId(token);
		AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("exchange", as.exchangeList());
		m.addAttribute("news", as.getNews());
		m.addAttribute("event", serv.countEvents());
		
		return "/admin/chiefDashboard";
	}
	
}
