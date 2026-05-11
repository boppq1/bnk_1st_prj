package com.example.demo.personal.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.demo.personal.dao.LoginAndRegisterIDAO;
import com.example.demo.personal.dto.UserRegistDTO;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class LoginAndRegisterService {

	private final LoginAndRegisterIDAO dao;
	
	public void register(UserRegistDTO dto) {
		if(dto == null) {
			dto.setPassword(passworEncryption(dto.getPassword()));
			if(dto.getPassword() == null) {
				
			}
		}
	}
	
	// 비밀번호 암호화
	public String passworEncryption(String password) {
		String encryptPassword = BCrypt.hashpw(password, password);
		if(encryptPassword == null) {
			return encryptPassword; 
		}
		return null;
	}
	
	// 비밀번호 복호화
	public boolean passwordDecryption(String loginPassword, String dbPassword) {
		if(BCrypt.checkpw(loginPassword, dbPassword)) {
			return true;
		}
		return false;
	}
}
