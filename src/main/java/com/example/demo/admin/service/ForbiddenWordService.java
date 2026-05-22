package com.example.demo.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.example.demo.admin.dao.IListDao;
import com.example.demo.admin.dto.KeywordBanDto;

@Service
public class ForbiddenWordService {
	
	@Autowired
	IListDao dao;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	// 프록시 객체 꺼내기
	private ForbiddenWordService getSelf() {
		return applicationContext.getBean(ForbiddenWordService.class);
	}
	
	// getSelf()를 통해 스프링 프록시 거쳐 호출 -> 서버 켜질 때 레디스에 저장
	@EventListener(ApplicationReadyEvent.class)
    public void initForbiddenWordsCache() {
        getSelf().getAllForbiddenWords();
    }
	
	@Cacheable(value = "forbiddenWords")
	public List<KeywordBanDto> getAllForbiddenWords() {
		System.out.println("Oracle DB에서 금지어 목록을 불러옵니다.");
		return dao.getKeywordBanList();
	}
	
	// 사용자가 입력한 검색어에 금지어가 포함되어 있는지 검사 (필터에서 해당 메서드 호출)
	public boolean isForbidden(String keyword) {
		if(keyword == null || keyword.isBlank()) {
			return false;
		}
		System.out.println("키워드 : " + keyword);
		String trimKeyword = keyword.trim();
		// 프록시를 통해 호출 -> 레디스에서 꺼내옴!
		List<KeywordBanDto> banList = getSelf().getAllForbiddenWords();
		
		// 완전히 일치하는지 비교 (만약 문장에 단어가 포함된 경우 막으려면 forbiddenList.stream().anyMatch(keyword::contains) 사용
		return banList.stream().anyMatch(dto -> dto.getKeyword().equals(trimKeyword));
	}
	
	
}
