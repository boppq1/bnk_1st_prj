package com.example.demo.personal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.personal.dao.IMyPageDAO;
import com.example.demo.personal.dto.MyPageAcntDTO;
import com.example.demo.personal.dto.MyPageFAcntDTO;
import com.example.demo.personal.dto.MyPageUserDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@Service
@Slf4j
public class MyPageService {

	private final IMyPageDAO dao;
	public Map<String, Object> getUserInfo(String id) {
		MyPageUserDTO userInfo = dao.selectUSerInfo(id);
		List<MyPageAcntDTO> userAcnt = dao.selectAcntInfo(userInfo.getUsr_no());
		List<MyPageFAcntDTO> userFAcnt = dao.selectFacntInfo(userInfo.getUsr_no());
		log.debug("마이페이지 유저 정보 {}",userInfo);
		log.debug("마이페이지 유저 계좌 정보 {}", userAcnt);
		log.debug("마이페이지 유저 해외 계좌 정보 {} ", userFAcnt);
		Map<String, Object> map = new HashMap<>();

		map.put("userInfo", userInfo);
		map.put("userAcnt", userAcnt);
		map.put("userFAcnt", userFAcnt);

		return map;	
	}
	
	public int 휴대폰버호확인(String tel) {
		int user = dao.checkPhonUser(tel);
		int company = dao.checkPhonCompany(tel);
		if(user == 0 && company == 0) {
			return 0;
		}
		return 1;
	}
}
