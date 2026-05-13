package com.example.demo.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.admin.dto.ExchangeRequestDto;

@Mapper
public interface IAdminExchangeDao {
	public List<ExchangeRequestDto> exchangeList();
}
