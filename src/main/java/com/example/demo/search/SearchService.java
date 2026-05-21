package com.example.demo.search;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

	private final SearchDao searchDao;

	/**
	 * 전체 추천 검색어 (SEARCH_VOLUME 내림차순) 검색창 열릴 때 인기 검색어 칩 표시용
	 */
	public List<SearchDto> findAllOrderByVolume() {
		return searchDao.selectAll();
	}

	/**
	 * 입력 글자 포함 검색 (순서 무관, 부분 일치) 예) "외" 입력 시 "외환", "외화송금" 모두 반환
	 *
	 * DB LIKE 방식과 Java 필터 방식 중 선택 가능 - 데이터 적을 때 → Java 필터 (주석 처리된 부분) - 데이터 많을 때 →
	 * DB LIKE 방식 (현재 기본값, XML 참고)
	 */
	public List<SearchDto> findByKeywordChars(String query) {
		
		// ── 방법 1: DB에서 LIKE로 검색 (기본값) ──────────────────
		return searchDao.selectByKeyword(query);

		// ── 방법 2: 전체 조회 후 Java에서 글자 단위 필터 ─────────
		// 순서 무관 부분 일치가 필요하면 이 방식 사용
		// (ex. "환외" 입력해도 "외환" 검색됨)
		/*
		 * List<SuggestedSearchDto> all = suggestedSearchDao.selectAll(); char[] chars =
		 * query.toCharArray();
		 * 
		 * return all.stream() .filter(dto -> { for (char c : chars) { if
		 * (dto.getKeyword().indexOf(c) < 0) return false; } return true; })
		 * .collect(java.util.stream.Collectors.toList());
		 */
	}

}
