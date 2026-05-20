package com.example.demo.personal.dao;



import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.personal.dto.MyPageAcntDTO;
import com.example.demo.personal.dto.MyPageFAcntDTO;
import com.example.demo.personal.dto.MyPageUserDTO;

import io.lettuce.core.dynamic.annotation.Param;

@Mapper
public interface IMyPageDAO {
	MyPageUserDTO selectUSerInfo(@Param("login_id")String login_id);
	List<MyPageAcntDTO> selectAcntInfo(@Param("usr_no") Long usr_no);
	List<MyPageFAcntDTO> selectFacntInfo(@Param("usr_no") Long usr_no);
	int checkPhonUser(@Param("tel_no")String tel_no);
	int checkPhonCompany(@Param("tel_no")String tel_no);
}
