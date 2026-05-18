package com.example.demo.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.demo.admin.dao.IListDao;
import com.example.demo.admin.dto.KeywordBanDto;

@Service
public class ForbiddenWordService {
	
	@Autowired
	IListDao dao;
	
	@Cacheable(value = "forbiddenWords")
	public List<KeywordBanDto> getAllForbiddenWords() {
		System.out.println("캐시가 비어있어 Oracle DB에서 금지어 목록을 불러옵니다.");
		
		return dao.getKeywordBanList();
	}
	
	// 사용자가 입력한 검색어에 금지어가 포함되어 있는지 검사 (필터에서 해당 메서드 호출)
	public boolean isForbidden(String keyword) {
		if(keyword == null || keyword.isBlank()) {
			return false;
		}
		
		// 스프링 캐시 메모리에 있는 금지어 리스트 가져옴
		List<KeywordBanDto> banList = getAllForbiddenWords();
		
		// 완전히 일치하는지 비교 (만약 문장에 단어가 포함된 경우 막으려면 forbiddenList.stream().anyMatch(keyword::contains) 사용
		return banList.contains(keyword.trim());
	}
	
	
}
