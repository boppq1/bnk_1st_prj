package com.example.demo.company.controller;


import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.company.service.CompanyMyPageService;
import com.example.demo.jwt.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequiredArgsConstructor
public class CMyPageController {

	private final JwtUtil jwt;
	private final CompanyMyPageService service;
	
	@GetMapping("/company/companyMyPage")
	public String getCompanyMyPage(Model model,HttpServletRequest request, HttpSession session) {
		System.out.println("기업마이페이지컨트롤러");
		Cookie[] cookies = request.getCookies();
		if(cookies == null) {
			System.out.println("쿠키없음");
		}
		for(Cookie cookie : cookies) {
			if ("accessToken".equals(cookie.getName())) {
				
				String token = cookie.getValue();		
				String id = jwt.getLoginId(token);
				String role = jwt.getRole(token);
				log.debug("아이디 {} role {}", id, role);
				Map<String, Object> info = service.getUserInfo(id);
				model.addAttribute("userInfo", info.get("userInfo"));
				model.addAttribute("userAcnt", info.get("userAcnt"));
				model.addAttribute("userFAcnt", info.get("userFAcnt"));
				
				session.setAttribute("userInfo", info.get("userInfo"));
				session.setAttribute("userAcnt", info.get("userAcnt"));
				session.setAttribute("userFAcnt", info.get("userFAcnt"));
			}
		} 
		log.info("개인마이페이지");
		return "company/companyMyPage";
	}
}
