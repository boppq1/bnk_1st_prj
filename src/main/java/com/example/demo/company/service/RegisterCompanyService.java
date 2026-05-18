package com.example.demo.company.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.company.dao.ICompanyRegisterDAO;
import com.example.demo.company.dto.AccountsCompanyDTO;
import com.example.demo.company.dto.CompanyDto;
import com.example.demo.company.dto.CompanyUserDTO;
import com.example.demo.company.dto.ForeignAccountsCompanyDTO;
import com.example.demo.personal.service.LoginAndRegisterService;
import com.example.demo.randomNumber.RandomAccount_no;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class RegisterCompanyService {
	
	private final RandomAccount_no getAccount;
	private final LoginAndRegisterService userService;
	private final ICompanyRegisterDAO companyDAO;
	
	// 사업자 번호 확인하기
	public boolean selectBno(String bno) {
		if(companyDAO.selectBno(bno) == 1) {
			System.out.println("서비스에서 사업자 번호 확인 : "+ bno);
			return true;			
		}
		return false;
	}
	
	// 기업 회원등록
	@Transactional
	public String insertCompanyUser(CompanyUserDTO dto) {
		if (dto != null) {
            String encryptedPassword = userService.passwordEncryption(dto.getPassword());
            dto.setPassword(encryptedPassword);
            System.out.println("서비스에서 ----------------------------- dto");
            System.out.println(dto.toString());
          
            CompanyDto companies = new CompanyDto();
            companies = companyDAO.selectCompany(companies.getBusiness_no());
            dto.setCompany_id(companies.getCompany_id());
            System.out.println(dto.toString());
            dto.setStatus("정상");
            companyDAO.insertCompanyUser(dto);
            String newAcc;
            while(true) {
            	newAcc = getAccount.getAccountNumber();
            	if(companyDAO.selectDuplAcc(newAcc) == 0) {
            		break;
            	}
            }
            //계좌번호 암호화
            String newAccPw = userService.passwordEncryption(dto.getAccount_pw());
            AccountsCompanyDTO newAC = new AccountsCompanyDTO();
            newAC.setAccount_pw(newAccPw);
            newAC.setBank_name("BNK");
            newAC.setAccount_no(newAcc);
            //기업 아이디 받아오는곳 
            CompanyDto companyInfo = companyDAO.selectCompany(dto.getBusiness_no());
            if(companyInfo == null) {
            	System.out.println("회사정보 불러오는 서비스에서 오류");
            	return "회상정보오류서비스단";
            }
            newAC.setCompany_id((long) companyInfo.getCompany_id());
            newAC.setBalance((long) 0);
            newAC.setCurrency("KRW");
            newAC.setAccount_status("정상");
            newAC.setLimit_one_time((long) 500000000);
            newAC.setLimit_daily(1000000000L);
            
            if(companyDAO.insertAC(newAC) != 1) {
            	System.out.println("기업국내계좌 넣을떄 오류");
            }
            
            ForeignAccountsCompanyDTO newFAC = new ForeignAccountsCompanyDTO();
            newFAC.setAccount_pw(newAccPw);
            newFAC.setBank_name("BNK");
            
            while(true) {
            	newAcc = getAccount.getAccountNumber();
            	if(companyDAO.selectDuplAcc(newAcc) == 0) {
            		break;
            	}
            }
            newFAC.setAccount_no(newAcc);
            newFAC.setCompany_id((long) companyInfo.getCompany_id());
            newFAC.setAccount_status("정상");
            newFAC.setLimit_one_time((long) 500000000);
            newFAC.setLimit_daily(1000000000L);
            
            if(companyDAO.insertFAC(newFAC) != 1) {
            	System.out.println("기업 해외계좌 넣을때 오류");
            }
            return "성공";
        }
		return "실패";
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
