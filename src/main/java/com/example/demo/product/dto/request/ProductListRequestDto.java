package com.example.demo.product.dto.request;

import lombok.Data;

//리스트 검색 필터, 정렬, 페이징용, 상품 리스트 조회 요청 DTO

@Data
public class ProductListRequestDto {
	private String targetLarge;// 고객 유형: personal(개인), company(기업), all(전체)
	private String targetDetail; // 고객 유형 소분류: 나이대로 분류
    private String productType;    // 상품 유형: 예금, 적금, 외화예금
    private String rateCategory;   // 금리 카테고리: 국내, 외환
    private String keyword;		   // 검색 키워드: 상품명, 상품 설명 검색용
    private String sort;           // 정렬 조건: latest(최신순), rate(금리순), name(이름순)
    private String foreignOnly; // 외환만 조회
    private int page = 1;		// 현재 페이지 번호
    private int size = 10;		// 페이지당 조회 갯수

    private int startRow;		//Oracle 페이징 시작 row
    private int endRow;			//Oracle 페이징 끝 row
    
    // 페이징 계산
    public void calculatePaging() {
        this.startRow = (page - 1) * size + 1;
        this.endRow = page * size;
    }
}
