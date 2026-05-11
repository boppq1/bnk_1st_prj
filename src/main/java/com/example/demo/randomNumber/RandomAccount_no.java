package com.example.demo.randomNumber;

import java.util.Random;

import org.springframework.stereotype.Component;
@Component
public class RandomAccount_no {

	public String getAccountNumber() {
			Random random = new Random();
			
			String accountNumber = "";
			
			for(int i=0; i<=12; i++) {
				accountNumber += random.nextInt(9)+1;
			}
			return accountNumber;
		}
}
