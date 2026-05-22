package com.example.demo.company.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.company.dto.AccountsCompanyDTO;
import com.example.demo.company.dto.CompanyUserDTO;
import com.example.demo.company.dto.ForeignAccountsCompanyDTO;

@Mapper
public interface ICompanyMyPage {
	CompanyUserDTO selectCUser(@Param("id")String id);
	List<AccountsCompanyDTO> selectAcnt(@Param("id")String id);
	List<ForeignAccountsCompanyDTO> selectFAnt(@Param("id")String id);
	
}
