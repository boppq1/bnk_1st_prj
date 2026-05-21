package com.example.demo.commonLogin.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.commonLogin.dao.ILoginDAO;
import com.example.demo.commonLogin.dto.UserLoginDTO;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.personal.service.LoginAndRegisterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {

	private final ILoginDAO loginDAO;
	private final JwtUtil jwt;
	private final LoginAndRegisterService service;

	public Map<String, String> login(UserLoginDTO dto) {

		if (dto == null) {
			return Map.of("result", "fail");
		}

		log.debug("로그인 서비스 파일 - ID: {}", dto.getLogin_id());

		UserLoginDTO getUserDTO = loginDAO.selectUser(dto.getLogin_id());
		if (getUserDTO != null) {
			if (service.matchesPassword(dto.getSecu_pw(), getUserDTO.getSecu_pw())) {
				//로그인시간
				String nowStrDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				
				if(loginDAO.updateLoginTime(nowStrDate,getUserDTO.getUsr_no()) != 1) {
					log.debug("로그인 pk 못차아서 실패");
					return Map.of("result", "fail");
				}
				log.info("로그인시간 {}", nowStrDate);
				Map<String, Object> info = new HashMap<>();
				info.put("usr_nm", getUserDTO.getUsr_nm());
				info.put("role", "user");
				info.put("login_id",dto.getLogin_id());
				
				String token = jwt.generateToken(getUserDTO.getUsr_nm(), info);
				log.info("개인 회원 로그인 서비스 성공");
				log.info("유저 정보 {}", jwt.getUsername(token));
				return Map.of("token", token);
			}
			log.error("입력받은 비밀번호 {}, 데이터베이스 비밀번호 {} 매치확인 비밀번호", dto.getSecu_pw(), getUserDTO.getSecu_pw(), service.matchesPassword(dto.getSecu_pw(), getUserDTO.getSecu_pw()));
			log.error("로그인 실패: 개인회원 비밀번호 불일치  ID: {}", dto.getLogin_id());
			return Map.of("result", "fail");
		}

		UserLoginDTO getComUserDTO = loginDAO.selectCompanyUser(dto.getLogin_id());

		if (getComUserDTO != null) {

			if (service.matchesPassword(dto.getSecu_pw(), getComUserDTO.getSecu_pw())) {
				String com_nm = loginDAO.selectCompany(dto.getCom_no());
				String nowStrDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				if(loginDAO.updateCompanyLoginTime(getComUserDTO.getLogin_id(), nowStrDate) != 1) {
					log.debug("기업 로그인 시간 실패");
					return Map.of("result", "fail");
				}
				Map<String, Object> info = new HashMap<>();
				info.put("usr_nm", getComUserDTO.getUsr_nm());
				info.put("role", "company");
				info.put("company_nm", com_nm);
				info.put("login_id",dto.getLogin_id());
				String token = jwt.generateToken(getComUserDTO.getUsr_nm(), info);
				log.info("기업 회원 로그인 서비스 성공");
				return Map.of( "token",  token);
			}

			log.error("로그인 실패: 기업회원 비밀번호 불일치  ID: {}", dto.getLogin_id());
			return Map.of("result", "fail");
		}

		log.error("로그인 실패: 존재하지 않는 아이디 로그인 시도 : {}", dto.getLogin_id());
		return Map.of("result", "fail");
	}
}