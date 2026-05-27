package com.example.demo.admin.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.admin.dto.AdminDto;
import com.example.demo.admin.dto.BlacklistDto;
import com.example.demo.admin.service.AdminMergeService;
import com.example.demo.admin.service.BlacklistService;
import com.example.demo.interceptor.AdminLog;
import com.example.demo.jwt.JwtUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BlacklistController {
	
	private final BlacklistService bs;
	private final StringRedisTemplate stringRedisTemplate;
	private final JwtUtil jwt;
	private final AdminMergeService mergeServ;
	
	
	public void insertBlacklist(String clientKey, String reason) {
		String ip_addr = clientKey.split("_")[0];
		bs.insertBlacklist(clientKey, ip_addr, reason);
	}
	
	@AdminLog(action="블랙리스트 확인")
	@GetMapping("/admin/adminBlacklist")
	public String adminBlackListPage(@CookieValue(value = "accessToken") String token, Model m) {
		String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
		m.addAttribute("admin", dto);
		m.addAttribute("black", bs.getBlacklist());
		return "/admin/adminBlacklist";
	}
	
	@AdminLog(action="블랙리스트 제거")
	@GetMapping("/admin/liftBlack")
	public String liftBlack(BlacklistDto dto) {
		bs.liftBlack(dto);
		String redisKey = "blacklist:" + dto.getCli_key();
		System.out.println(redisKey);
		stringRedisTemplate.delete(redisKey);
		return "redirect:/admin/adminBlacklist";
	}
	
}
