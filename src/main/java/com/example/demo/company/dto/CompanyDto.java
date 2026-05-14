package com.example.demo.company.dto;


import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
@Data
public class CompanyDto {
	private int company_id;
	private String business_no;
	private String company_name;
	private String status;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String created_at;
}
