package com.example.demo.company.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.company.dao.ICompanyMyPage;
import com.example.demo.company.dto.AccountsCompanyDTO;
import com.example.demo.company.dto.CompanyUserDTO;
import com.example.demo.company.dto.ForeignAccountsCompanyDTO;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.personal.service.LoginAndRegisterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@Service
@Slf4j
public class CompanyMyPageService {
	private final ICompanyMyPage dao;
	private final JwtUtil jwt;
	private final LoginAndRegisterService service;
	
	public Map<String, Object> getUserInfo(String id) {
		CompanyUserDTO userInfo = dao.selectCUser(id);
		List<AccountsCompanyDTO> userAcnt = dao.selectAcnt(id);
		List<ForeignAccountsCompanyDTO> userFAcnt = dao.selectFAnt(id);
		
		log.debug("마이페이지 유저 정보 {}",userInfo);
		log.debug("마이페이지 유저 계좌 정보 {}", userAcnt);
		log.debug("마이페이지 유저 해외 계좌 정보 {} ", userFAcnt);
		Map<String, Object> map = new HashMap<>();

		map.put("userInfo", userInfo);
		map.put("userAcnt", userAcnt);
		map.put("userFAcnt", userFAcnt);

		return map;	
	}
}
