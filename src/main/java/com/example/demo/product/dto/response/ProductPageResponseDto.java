package com.example.demo.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


// 상품 페이징 응답 DTO,  페이징 정보 포함 응답

@Data
@AllArgsConstructor
public class ProductPageResponseDto {

    private List<ProductListResponseDto> products; // 상품 리스트 

    private int page; // 현재 페이지

    private int size; // 페이지당 개수

    private int totalCount; // 총 개수

    private int totalPage; // 총페이지
}