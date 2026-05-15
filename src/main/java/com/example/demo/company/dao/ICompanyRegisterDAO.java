package com.example.demo.company.dao;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.company.dto.AccountsCompanyDTO;
import com.example.demo.company.dto.CompanyDto;
import com.example.demo.company.dto.CompanyUserDTO;
import com.example.demo.company.dto.ForeignAccountsCompanyDTO;

@Mapper
public interface ICompanyRegisterDAO {

	// 사업자 번호 조희
	int selectBno(@Param("business_no") String bno);
	// 국내계좌조회 해외계좌 기업계좌 개인계좌
	int selectDuplAcc(@Param("account_no")String ano);
	CompanyDto selectCompany(@Param("business_no") String bno);
	//아이디 중복조회
	int checkId(@Param("Login_id") String Login_id);
	
	// 회원가입 C
	int insertCompanyUser(CompanyUserDTO dto);
	int insertAC(AccountsCompanyDTO dto);
	int insertFAC(ForeignAccountsCompanyDTO dto);
	
	
}
