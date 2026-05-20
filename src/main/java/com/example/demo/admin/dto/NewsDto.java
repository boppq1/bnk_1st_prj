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
public class NewsDto {
	Long news_no;
	String name;
	String news_ttl;
	String news_cont;
	Long news_wtr_no;
	String news_dt;
	Long views;
}
