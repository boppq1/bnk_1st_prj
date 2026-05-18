package com.example.demo.personal.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.personal.dao.LoginAndRegisterIDAO;
import com.example.demo.personal.dto.Accounts_personalDTO;
import com.example.demo.personal.dto.UserRegistDTO;
import com.example.demo.randomNumber.RandomAccount_no;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LoginAndRegisterService {
	
	private final RandomAccount_no getAccount;
	private final LoginAndRegisterIDAO dao;
	// 부족한점 외국계좌번호랑 국내계좌번호 다르게 넣어야하는데 일단 나중에 ~~ 기업은 제대로 만들거
	@Transactional 
    public int register(UserRegistDTO dto) {
        if (dto != null) {
            String encryptedPassword = passwordEncryption(dto.getPassword());
            dto.setPassword(encryptedPassword);
            System.out.println("서비스에서 ----------------------------- dto");
            System.out.println(dto.toString());

            	dao.insertUser(dto);				

            Accounts_personalDTO account = new Accounts_personalDTO();
            String encryptedAcPassword = passwordEncryption(dto.getAccount_pw());
            account.setAccount_pw(encryptedAcPassword);
            account.setBank_name("BNK");
            
            String accountNumber;
            while(true) {
                accountNumber = getAccount.getAccountNumber(); 
                if(dao.checkAccount(accountNumber) == 0) {    
                    break; 
                }
            }
            account.setAccount_no(accountNumber);
            account.setAccount_no(accountNumber);
            account.setAccount_status("정상");
            account.setCurrency("KRW");
            account.setBalance((long) 0);
            account.setLimit_one_time((long) 100);
            account.setLimit_daily((long) 300);
            
            dao.insertAccount(account);
            dao.insertFxAccount(account);
            return 1;
        }
        return 0;
    }
	
	// 비밀번호 암호화
	public String passwordEncryption(String password) {
	    return BCrypt.hashpw(password, BCrypt.gensalt());
	}
	
	// 비밀번호 일치확인
	public boolean matchesPassword(String loginPassword, String dbPassword) {
		if(BCrypt.checkpw(loginPassword, dbPassword)) {
			return true;
		}
		return false;
	}
	// 아이디 중복확인
	public boolean checkId(String id) {
		System.out.println("company 서비스 받은 id" + id);
		System.out.println();
		if(dao.checkId(id) == 1) {
			return true;
		}
		return false;
	}
}
