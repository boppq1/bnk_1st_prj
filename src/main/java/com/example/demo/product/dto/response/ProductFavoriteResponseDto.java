package com.example.demo.product.dto.response;
// 관심 상품
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductFavoriteResponseDto {
    private boolean success; // 응갑 성공 여부
    private boolean favorite; //현재 관심 상품 상태
    private String message; // 응답 메시지 
}
