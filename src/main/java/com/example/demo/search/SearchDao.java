package com.example.demo.search;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
 
@Mapper
public interface SearchDao {
 
    /**
     * 전체 추천 검색어 조회 (SEARCH_VOLUME 내림차순)
     */
    List<SearchDto> selectAll();
 
    /**
     * 키워드 포함 검색
     * XML에서 LIKE '%#{query}%' 처리
     */
    List<SearchDto> selectByKeyword(@Param("query") String query);
    
    int insertSearchKeyword(@Param("query") String query, @Param("searcher") String searcher,  @Param("role_type") String role_type);
}
 
