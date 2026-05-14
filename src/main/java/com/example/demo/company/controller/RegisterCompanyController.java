package com.example.demo.company.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.company.dto.CompanyUserDTO;
import com.example.demo.company.service.RegisterCompanyService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Controller
public class RegisterCompanyController {

	private final RegisterCompanyService service;
	
	@GetMapping("/companyRegisterPage")
	public String 기업회원가입페이지() {
		System.out.println("기업회원가입페이지 이동 컨트롤러");
		return "company/companyRegister";
	}
	
	@ResponseBody
	@PostMapping("/selectBno")
	public int 사업자번호조회(@RequestBody Map<String,String> business_no) {
		System.out.println("사업자번호조희 비동기 통신 컨트롤러");
		System.out.println(business_no.get("business_no"));
		if(service.selectBno(business_no.get("business_no")) == true) {
			return 200;
		}
		return 0;
	}
	@PostMapping("/companyrPro")
	public String 기업직원회원가입(CompanyUserDTO dto) {
		System.out.println("회사개인 정보 : "+dto.toString());
		if(service.insertCompanyUser(dto).equals("성공")) {
			return "redirect:/index";
		}
		return "redirect:/company/companyRegister";
	}
	
	@ResponseBody
	@PostMapping("/companyCheckId")
	public ResponseEntity<Map<String, String>> 아이디중복확인(@RequestBody Map<String, String> Login_id) {
		System.out.println(Login_id.get("login_id"));
		
		String id =Login_id.get("login_id");
		System.out.println("company 컨트롤러 id  : "+id);
		
		if(service.checkId(Login_id.get("login_id")) == true){
			return ResponseEntity.ok(Map.of("result","중복된아이디"));
		}
			
		return ResponseEntity.ok(Map.of("result","사용가능한아이디"));
	}
}
