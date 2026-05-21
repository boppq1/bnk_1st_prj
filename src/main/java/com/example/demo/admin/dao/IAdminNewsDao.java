package com.example.demo.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.admin.dto.NewsDto;

@Mapper
public interface IAdminNewsDao {
	List<NewsDto> getNews();
	int makeNews(@Param("news_ttl") String news_ttl, @Param("news_cont") String news_cont, @Param("news_wtr_no") Long news_wtr_no);
	NewsDto getOneNews(@Param("news_no") Long news_no);
	int updateNews(@Param("news_ttl") String news_ttl, @Param("news_cont") String news_cont);
	int deleteNews(@Param("news_no") Long news_no);
}
