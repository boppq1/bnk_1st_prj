package com.example.demo.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFavoriteRequestDto {
	 private String usrType;    // PERSONAL 또는 COMPANY(개인인지 기업안자 구분하는 값)
	 private Long usrNo;        // 사용자 번호
	 private Long productId;    // 상품 번호 (관심 상품으로 등록하거나 삭제될 번호)
}
