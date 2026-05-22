package com.example.demo.personal.controller;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.personal.dto.MyPageUserDTO;
import com.example.demo.personal.service.MyPageService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequiredArgsConstructor
public class MyPageController {
	private final MyPageService service;
	private final JwtUtil jwt;
	
	@GetMapping("/personal/myPage")
	public String 마이페이지(Model model,HttpServletRequest request, HttpSession session) {
		Cookie[] cookies = request.getCookies();

		for(Cookie cookie : cookies) {
			if ("accessToken".equals(cookie.getName())) {
				String token = cookie.getValue();
				String id = jwt.getLoginId(token);
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
		return "personal/myPage";
	}
	
	@GetMapping("/personal/changeUserInfo")
	public String 사용자정보변경페이지(HttpSession session, Model model) {
		Object userInfo = session.getAttribute("userInfo"); 
		
		model.addAttribute("userInfo", userInfo);
		return "personal/changeUserInfo";
	}
	
	@GetMapping("/checkPhon")
	@ResponseBody
	public ResponseEntity<String> 사용자정보면경휴대폰만확인(@RequestParam("tel_no") String telNo) {
		log.debug("사용자 정보변경 {}", telNo);
		if(service.휴대폰버호확인(telNo) == 0 ) {
			return ResponseEntity.ok("가능"); 
		}
		return ResponseEntity.status(409).body("불가능");
	}
	
	@PostMapping("/changeUserInfo")
	@ResponseBody
	public String 사용정보변경(@RequestBody MyPageUserDTO dto) {
		System.out.println("정보컨트롤러");
		System.out.println(dto);
		if(service.사용자정보변경(dto).equals("성공")){
			return "성공"; 
		}
		return null;
		
	}
	
	@GetMapping("/changePassword")
	public String 개인비밀번호변경페이지() {
		
		return "personal/changePassword";
	}
	@ResponseBody
	@PostMapping("/changePwProc")
	public ResponseEntity<String> changePwProc(@RequestBody Map<String, String> body, HttpServletRequest request) {
		log.debug("새비밀번호 {}", body.get("new_pw"));
		log.debug("현재비밀번호 {}", body.get("now_pw"));
		if(service.checkPw(body.get("new_pw"),request) == true) {
			
			return ResponseEntity.ok().body("ok");
		}
		
		//변경안댐
		return ResponseEntity.ok().body("fail");
	}
}
