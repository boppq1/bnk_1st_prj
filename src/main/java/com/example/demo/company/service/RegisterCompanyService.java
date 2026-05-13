package com.example.demo.company.service;

import org.springframework.stereotype.Service;

import com.example.demo.company.dao.ICompanyRegisterDAO;
import com.example.demo.company.dto.CompanyUserDTO;
import com.example.demo.personal.dao.LoginAndRegisterIDAO;
import com.example.demo.personal.dto.Accounts_personalDTO;
import com.example.demo.personal.service.LoginAndRegisterService;
import com.example.demo.randomNumber.RandomAccount_no;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class RegisterCompanyService {
	
	private final RandomAccount_no getAccount;
	private final LoginAndRegisterService userService;
	private final ICompanyRegisterDAO companyDAO;
	private final LoginAndRegisterIDAO userDAO;
	// 사업자 번호 확인하기
	public boolean selectBno(String bno) {
		if(companyDAO.selectBno(bno) == 1) {
			System.out.println("서비스에서 사업자 번호 확인 : "+ bno);
			return true;			
		}
		return false;
	}
	
	// 기업 회원등록
	public void insertCompanyUser(CompanyUserDTO dto) {
		if (dto != null) {
            String encryptedPassword = userService.passwordEncryption(dto.getPassword());
            dto.setPassword(encryptedPassword);
            System.out.println("서비스에서 ----------------------------- dto");
            System.out.println(dto.toString());
            companyDAO.insertCompanyUser(dto);
            Accounts_personalDTO account = new Accounts_personalDTO();
            
         
        }
	}
	
	// 아이디 중복확인
		public boolean checkId(String id) {
			System.out.println("서비스 받은 id" + id);
			System.out.println();
			if(companyDAO.checkId(id) == 1) {
				return true;
			}
			return false;
		}
}
