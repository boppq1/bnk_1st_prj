package com.example.demo.personal.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.personal.dto.UserRegistDTO;
import com.example.demo.personal.service.LoginAndRegisterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@Controller
@Slf4j
public class LoginAndRegisterController {
	
	private final LoginAndRegisterService registerService;
	
	@GetMapping("/registerPage")
	public String 회원가입페이지() {
		return "personal/registerPage";
	}
	
	@PostMapping("/registerPro")
	public String 회원가입(UserRegistDTO dto) {
		log.debug("회원가입 컨트롤러 dto {}", dto.toString());
		
		if(registerService.register(dto) == 1) {
			log.debug("회원가입 성공시 메인페이지로");
			return "redirect:/index";
		}
		return "redirect:/registerPage";
	}
	
	@ResponseBody
	@PostMapping("/checkId")
	public ResponseEntity<Map<String, String>> 아이디중복확인(@RequestBody Map<String, String> Login_id) {
		String id =Login_id.get("login_id");
		
		if(registerService.checkId(Login_id.get("login_id")) == true){
			return ResponseEntity.ok(Map.of("result","중복된아이디"));
		}
			
		return ResponseEntity.ok(Map.of("result","사용가능한아이디"));
	}
	
}
