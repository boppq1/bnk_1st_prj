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
public class SearchLogDto {
	Long search_no;
	String search_time;
	Long searcher;
	String role_type;
	String keyword;
	String search_volume;
}
