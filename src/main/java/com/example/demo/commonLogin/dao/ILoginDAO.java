package com.example.demo.commonLogin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.commonLogin.dto.UserLoginDTO;

@Mapper
public interface ILoginDAO {
	
	// 개인회원테이블에서 검색
	UserLoginDTO selectUser(@Param("login_id")String id);
	// 기업회원테이블에서 검색
	UserLoginDTO selectCompanyUser(@Param("login_id")String id);
}
