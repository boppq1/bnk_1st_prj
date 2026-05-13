package com.example.demo.admin.dao;

import com.example.demo.admin.dto.AdminActionLogDto;
import com.example.demo.admin.dto.ApiLogDto;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAdminLogDao {
    List<AdminActionLogDto> adminLog();
    List<ApiLogDto> userLog();
}
