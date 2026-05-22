package com.example.demo.admin.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.admin.dto.AdminDto;
import com.example.demo.admin.dto.BlacklistDto;
import com.example.demo.admin.service.BlacklistService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BlacklistController {
	
	private final BlacklistService bs;
	private StringRedisTemplate stringRedisTemplate;
	
	public void insertBlacklist(String clientKey, String reason) {
		String ip_addr = clientKey.split("_")[0];
		bs.insertBlacklist(clientKey, ip_addr, reason);
	}
	
	@GetMapping("/admin/adminBlacklist")
	public String adminBlackListPage(HttpSession session, Model m) {
		AdminDto dto = (AdminDto) session.getAttribute("admin");
		m.addAttribute("admin", dto);
		m.addAttribute("black", bs.getBlacklist());
		return "/admin/adminBlacklist";
	}
	
	@GetMapping("/admin/liftBlack")
	public String liftBlack(BlacklistDto dto) {
		bs.liftBlack(dto);
		String redisKey = "blacklist:" + dto.getCli_key();
		stringRedisTemplate.delete(redisKey);
		return "redirect:/admin/adminBlacklist";
	}
	
}
