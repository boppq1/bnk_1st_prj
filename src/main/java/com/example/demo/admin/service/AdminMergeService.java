package com.example.demo.admin.service;

import com.example.demo.admin.dao.IAdminDao;
import com.example.demo.admin.dto.AdminDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminMergeService{

    private final IAdminDao dao;

    // 관리자 회원가입
    public int join(AdminDto dto) {
        return dao.join(dto);
    }

    // 관리자 로그인
    public AdminDto login(AdminDto dto) {
        AdminDto admin = dao.login(dto);

        if(admin == null){
            throw new RuntimeException("아이디 조회 안 됨");
        }

        if(!admin.getPassword().equals(dto.getPassword())){
            throw new RuntimeException("비밀번호 불일치");
        }

        return admin;
    }

    // 내 정보 조회
    public AdminDto selectMyPage(AdminDto dto) {
        return dao.selectMyPage(dto);
    }

    // 내 정보 수정
    public int updateMyPage(AdminDto dto) {
        return dao.updateMyPage(dto);
    }



}
