package com.example.demo.personal.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.personal.dto.Accounts_personalDTO;
import com.example.demo.personal.dto.UserRegistDTO;

@Mapper
public interface LoginAndRegisterIDAO {
	// 회원가입
	int insertUser(UserRegistDTO dto);
	// 회원가입
	int insertAccount(Accounts_personalDTO ac);
	// 회원가입
	int checkAccount(@Param("account_no") String account_no);
}
