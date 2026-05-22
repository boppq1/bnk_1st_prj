package com.example.demo.company.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.company.dto.AccountsCompanyDTO;
import com.example.demo.company.dto.CompaniesDTO;
import com.example.demo.company.dto.CompanyUserDTO;
import com.example.demo.company.dto.ForeignAccountsCompanyDTO;

@Mapper
public interface ICompanyMyPage {
	CompanyUserDTO findCUser(@Param("login_id")String id);
	// 국내계좌는 기업 pk 로 찾아야함
	List<AccountsCompanyDTO> findAcnt(@Param("com_no")String id);
	// 해외계좌는 유저 id로
	List<ForeignAccountsCompanyDTO> findFAcnt(@Param("login_id")String id);
	CompaniesDTO findCompany(@Param("com_no")int no);
}
