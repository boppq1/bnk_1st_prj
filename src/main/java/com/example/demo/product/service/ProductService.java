package com.example.demo.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.product.dao.ProductDao;
import com.example.demo.product.dto.request.ProductListRequestDto;
import com.example.demo.product.dto.response.ProductDetailResponseDto;
import com.example.demo.product.dto.response.ProductListResponseDto;
import com.example.demo.product.dto.response.ProductPageResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	
	private final ProductDao dao;
//	상품 목록 조회 + 페이지네이션 처리
	public ProductPageResponseDto getProductList(ProductListRequestDto dto) {
//		DTO 안에서 : startRow 계산 endRow 계산
		dto.calculatePaging();
		
		List<ProductListResponseDto> products = dao.getProductList(dto);
		
		int totalCount = dao.getProductCount(dto);
//		총 페이지 개수 계산, Math.ceil : 올림 함수
        int totalPage = (int) Math.ceil((double) totalCount / dto.getSize());
//       상품 리스트 페이지에 필요한 데이터 한번에 묶어서 반환
        return new ProductPageResponseDto(products, dto.getPage(), dto.getSize(), totalCount, totalPage);
	}
	
	
	public ProductDetailResponseDto getProductDetail(Long productId) {
		ProductDetailResponseDto detail = dao.getProductDetail(productId);
//		통화, 금리, 우대금리 정보 불러오기
		detail.setCurrencies(dao.getCurrencies(productId));
		detail.setRates(dao.getRates(productId));
		detail.setPreferentialRates(dao.getPreferentialRates(productId));
		
		return detail;
	}
//	추천 상품
	public List<ProductListResponseDto> getRecommendProducts(ProductListRequestDto dto) {
	    return dao.getRecommendProducts(dto);
	}
}
