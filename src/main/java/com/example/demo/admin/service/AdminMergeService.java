package com.example.demo.admin.service;

import com.example.demo.admin.dao.IAdminActionDao;
import com.example.demo.admin.dao.IAdminDao;
import com.example.demo.admin.dto.AdminDto;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminMergeService {

    private final IAdminDao dao;
    private final IAdminActionDao actionDao;

    // =========================
    // 비밀번호 암호화
    // =========================
    public String passwordEncryption(String password) {

        return org.mindrot.jbcrypt.BCrypt.hashpw(
                password,
                org.mindrot.jbcrypt.BCrypt.gensalt()
        );
    }

    // =========================
    // 비밀번호 비교
    // =========================
    public boolean passwordDecryption(String loginPassword,
                                      String dbPassword) {

        return org.mindrot.jbcrypt.BCrypt.checkpw(
                loginPassword,
                dbPassword
        );
    }

    // =========================
    // 회원가입
    public int join(AdminDto dto) {

        // 비밀번호 암호화
        String encodePw =
                passwordEncryption(dto.getPassword());

        dto.setPassword(encodePw);

        // 관리자 비밀번호 암호화
        String encodeAdminPw =
                passwordEncryption(dto.getAdmin_pw());

        dto.setAdmin_pw(encodeAdminPw);

        return dao.join(dto);
    }

    // =========================
    // 로그인
    // =========================
    public AdminDto login(AdminDto dto) {

        // 아이디 조회
        AdminDto admin = dao.login(dto);

        System.out.println(admin);

        // 아이디 없음
        if(admin == null) {
            return null;
        }

        // 비밀번호 비교
        boolean match =
                passwordDecryption(
                        dto.getPassword(),
                        admin.getPassword()
                );

        System.out.println(match);

        // 로그인 성공
        if(match) {
            return admin;
        }

        // 로그인 실패
        return null;
    }

    // =========================
    // 내 정보 조회
    // =========================
    public AdminDto selectMyPage(AdminDto dto) {
        return dao.selectMyPage(dto);
    }

    // =========================
    // 내 정보 수정
    // =========================
    public void updateMyPage(AdminDto dto) {

        // 비밀번호 다시 암호화
        String encodePw =
                passwordEncryption(dto.getPassword());

        dto.setPassword(encodePw);

        dao.updateMyPage(dto);
    }

    // =========================
    // 내 비밀번호 수정
    // =========================

    public int updatePassword(Long admin_id,
                              String password) {

        String encodedPw =
                BCrypt.hashpw(password,
                        BCrypt.gensalt());

        return dao.updatePassword(admin_id,
                encodedPw);
    }

    // =========================
    // 관리자 비밀번호 수정
    // =========================

    public int updateAdminPw(Long admin_id,
                             String admin_pw) {

        // 관리자 비밀번호 암호화
        String encodedAdminPw =
                BCrypt.hashpw(admin_pw,
                        BCrypt.gensalt());

        return dao.updateAdminPw(admin_id,
                encodedAdminPw);
    }

}