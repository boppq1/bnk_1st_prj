package com.example.demo.admin.dao;

import com.example.demo.admin.dto.AdminLogDto;
import com.example.demo.admin.dto.ApiLogDto;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAdminLogDao {
    int insertLog(AdminLogDto adminLogDto);
    List<AdminLogDto> adminLog();
    List<ApiLogDto> userLog();
}
