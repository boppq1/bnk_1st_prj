package com.example.demo.personal.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.personal.dto.UserRegistDTO;

@Mapper
public interface LoginAndRegisterIDAO {
	int register(UserRegistDTO dto);
}
