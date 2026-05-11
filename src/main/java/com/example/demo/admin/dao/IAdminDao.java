package com.example.demo.admin.dao;

import com.example.demo.admin.dto.AdminDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAdminDao {
    AdminDto login(AdminDto adminDto);

    int join(AdminDto adminDto);

    AdminDto selectMyPage(AdminDto adminDto);

    int updateMyPage(AdminDto adminDto);

}
