package com.example.demo.commonLogin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.commonLogin.dto.CompanyLoginDTO;
import com.example.demo.commonLogin.dto.UserLoginDTO;

@Mapper
public interface ILoginDAO {
	
	// 개인회원테이블에서 검색
	UserLoginDTO selectUser(@Param("login_id")String id);
	// 기업회원테이블에서 검색
	CompanyLoginDTO selectCompanyUser(@Param("login_id")String id);
	// 기업 검색
	String selectCompany(@Param("com_no")int com_no);
	// 개인회원 로그인시 마지막 로그인 시간 
	int updateLoginTime(@Param("last_lgn_dt") String date, @Param("usr_no") int no);
	// 기업 회원 로그인 할때 로그인시간
	int updateCompanyLoginTime(@Param("login_id")String login_id, @Param("last_lgn_dt")String date);
}
