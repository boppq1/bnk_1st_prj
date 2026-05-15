package com.example.demo.admin.dto;

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
public class SuggestedSearchDto {
	Long suggested_no;
	String role_type;
	String keyword;
	Long search_volume;
}
