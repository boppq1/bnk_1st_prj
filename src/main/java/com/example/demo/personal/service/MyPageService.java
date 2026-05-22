package com.example.demo.personal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.personal.dao.IMyPageDAO;
import com.example.demo.personal.dto.MyPageAcntDTO;
import com.example.demo.personal.dto.MyPageFAcntDTO;
import com.example.demo.personal.dto.MyPageUserDTO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@Service
@Slf4j
public class MyPageService {

	private final IMyPageDAO dao;
	private final JwtUtil jwt;
	private final LoginAndRegisterService service;
	public Map<String, Object> getUserInfo(String id) {
		MyPageUserDTO userInfo = dao.selectUSerInfo(id);
		List<MyPageAcntDTO> userAcnt = dao.selectAcntInfo(userInfo.getUsr_no());
		List<MyPageFAcntDTO> userFAcnt = dao.selectFacntInfo(userInfo.getUsr_no());
		log.debug("마이페이지 유저 정보 {}",userInfo);
		log.debug("마이페이지 유저 계좌 정보 {}", userAcnt);
		log.debug("마이페이지 유저 해외 계좌 정보 {} ", userFAcnt);
		Map<String, Object> map = new HashMap<>();

		map.put("userInfo", userInfo);
		map.put("userAcnt", userAcnt);
		map.put("userFAcnt", userFAcnt);

		return map;	
	}
	
	public int 휴대폰버호확인(String tel) {
		int user = dao.checkPhonUser(tel);
		int company = dao.checkPhonCompany(tel);
		if(user == 0 && company == 0) {
			return 0;
		}
		return 1;
	}
	
	public String 사용자정보변경(MyPageUserDTO dto) {
		if(dao.updateUserInfo(dto) == 1) {
			return "성공";
		}
		return "실패";
	}
	
	public boolean checkPw(String pw, HttpServletRequest request) {
		System.out.println("서비스");
		String login_id = "";
		String dbPW ="";
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie c : cookies) {
				if("accessToken".equals(c.getName())) {
					String token = c.getValue();
					login_id = jwt.getLoginId(token);
					System.out.println("로그인 id : "+login_id);
					//기존비번이랑 새비번이 같다 변경불가
					dbPW = dao.checkPassword(login_id);
					System.out.println("디비 비밀번호" + dbPW);
				}
			}
			// 변경 안댐
			if(service.matchesPassword(pw, dbPW) == true) {
				return false;
			}
			
		}else {
			System.out.println("쿠키없음");
		}
			pw = service.passwordEncryption(pw);
			log.debug("변경할 비밀번호{}, 로그인아이디 {}",pw, login_id);
			if(dao.updatePassword(pw, login_id) != 1) {
				return false;
			}
			return true;
	}
}
