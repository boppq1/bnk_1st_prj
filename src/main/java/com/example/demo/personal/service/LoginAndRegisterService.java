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
	
	@Transactional 
    public int register(UserRegistDTO dto) {
        if (dto != null) {
            String encryptedPassword = passwordEncryption(dto.getPassword());
            dto.setPassword(encryptedPassword);
            System.out.println("서비스에서 ----------------------------- dto");
            System.out.println(dto.toString());
            dao.insertUser(dto);
            Accounts_personalDTO account = new Accounts_personalDTO();
            account.setAccount_pw(dto.getAccount_pw());
            account.setBank_name("부산은행");
            
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
            account.setBalance(0);
            account.setLimit_one_time(100);
            account.setLimit_daily(300);
            dao.insertAccount(account);
            return 1;
        }
        return 0;
    }
	
	// 비밀번호 암호화
	public String passwordEncryption(String password) {
	    return BCrypt.hashpw(password, BCrypt.gensalt());
	}
	
	// 비밀번호 복호화
	public boolean passwordDecryption(String loginPassword, String dbPassword) {
		if(BCrypt.checkpw(loginPassword, dbPassword)) {
			return true;
		}
		return false;
	}
}
