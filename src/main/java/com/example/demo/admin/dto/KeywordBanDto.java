package com.example.demo.admin.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class KeywordBanDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long key_no;
	private String keyword;
	private String ban_dt;
	private Long adm_no;
}
