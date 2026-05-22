package com.example.demo.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.admin.dao.IBlacklistDao;
import com.example.demo.admin.dto.BlacklistDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlacklistService {
	
	final IBlacklistDao dao;
	
	public void insertBlacklist(String clientKey, String ip_addr, String reason) {
		dao.insertBlacklist(clientKey, ip_addr, reason);
	}
	
	public List<BlacklistDto> getBlacklist() {
		return dao.getBlacklist();
	}
	
	public void liftBlack(BlacklistDto dto) {
		dao.liftBlack(dto);
	}
}