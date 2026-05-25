package com.example.demo.admin.dao;

import com.example.demo.admin.dto.AdminDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IAdminDao {
    AdminDto login(@Param("login_id") String login_id);

    int join(AdminDto adminDto);

    AdminDto selectMyPage(@Param("login_id") String login_id);
    void updateMyPage(AdminDto adminDto);

    int updatePassword(@Param("admin_id") Long admin_id, @Param("password") String password);

    int updateAdminPw(@Param("admin_id") Long admin_id,
            @Param("admin_pw") String admin_pw
    );
}
