package com.example.demo.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.admin.dto.BlacklistDto;

@Mapper
public interface IBlacklistDao {
	int insertBlacklist(@Param("clientKey") String clientKey, @Param("ip_addr") String ip_addr, @Param("reason") String reason);
	List<BlacklistDto> getBlacklist();
	List<BlacklistDto> getBlacklistFive();
	int liftBlack(BlacklistDto dto);
}
