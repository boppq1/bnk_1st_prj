package com.example.demo.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class SearchDto {

	private Long suggestedNo;
	private String roleType; // "ROLE_COMPANY" | "ROLE_PERSONAL"
	private String keyword;
	private int searchVolume;
	private String pageUrl; // ex. "/exchange/main"
	private String category; // ex. "외환" — 선택사항, 프론트 뱃지 표시용

}
