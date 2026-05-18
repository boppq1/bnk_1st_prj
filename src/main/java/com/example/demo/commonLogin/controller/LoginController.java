package com.example.demo.commonLogin.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.commonLogin.dto.UserLoginDTO;
import com.example.demo.commonLogin.service.LoginService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {
	
	private final LoginService service;
	
	@GetMapping("/loginPage")
	public String 로그인페이지로이동() {
		log.info("로그인페이지 컨트롤러");
		return "loginPage";
	}
	
	@ResponseBody
	@PostMapping("/loginProc")
	public Map<String, String> 로그인(@RequestBody UserLoginDTO dto, HttpServletResponse response) {
		Map<String, String> result =  service.login(dto);
		if(result.containsKey("token")) {
			log.debug("token : {}", result.get("token"));
			
			Cookie cookie = new Cookie("accessToken", result.get("token"));
			
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			cookie.setMaxAge(60*1);
			response.addCookie(cookie);
			
			return Map.of("result", "sucess", "expiresIn", "60000");
		}
		return Map.of("result", "fail");
	}
}
