package com.example.demo.product.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.product.dto.response.ProductFavoriteResponseDto;
import com.example.demo.product.service.ProductFavoriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/favorites")
public class ProductFavoriteApiController {

    private final ProductFavoriteService productFavoriteService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{productId}")
    public ProductFavoriteResponseDto toggleFavorite(
            @PathVariable("productId") Long productId,
            @CookieValue(value = "accessToken", required = false) String token
    ) {
        if (token == null || token.isBlank()) {
            return new ProductFavoriteResponseDto(
                    false,
                    false,
                    "로그인이 필요합니다."
            );
        }

        if (!jwtUtil.isValid(token)) {
            return new ProductFavoriteResponseDto(
                    false,
                    false,
                    "로그인 정보가 유효하지 않습니다."
            );
        }

        String role = jwtUtil.getRole(token);
        String loginId = jwtUtil.getLoginId(token);

        if (role == null || loginId == null) {
        	System.out.println("확인중--------------------"+role+loginId);
            return new ProductFavoriteResponseDto(
                    false,
                    false,
                    "로그인 정보를 확인할 수 없습니다."
            );
        }

        try {
            boolean favorite = productFavoriteService.toggleFavoriteByLoginUser(
                    role,
                    loginId,
                    productId
            );

            return new ProductFavoriteResponseDto(
                    true,
                    favorite,
                    favorite ? "관심상품에 등록되었습니다." : "관심상품에서 삭제되었습니다."
            );

        } catch (IllegalArgumentException e) {
            return new ProductFavoriteResponseDto(
                    false,
                    false,
                    e.getMessage()
            );
        }
    }

    @GetMapping("/{productId}/status")
    public ProductFavoriteResponseDto favoriteStatus(
            @PathVariable("productId") Long productId,
            @CookieValue(value = "accessToken", required = false) String token
    ) {
        if (token == null || token.isBlank()) {
            return new ProductFavoriteResponseDto(
                    false,
                    false,
                    "로그인이 필요합니다."
            );
        }

        if (!jwtUtil.isValid(token)) {
            return new ProductFavoriteResponseDto(
                    false,
                    false,
                    "로그인 정보가 유효하지 않습니다."
            );
        }

        String role = jwtUtil.getRole(token);
        String loginId = jwtUtil.getLoginId(token);

        if (role == null || loginId == null) {
            return new ProductFavoriteResponseDto(
                    false,
                    false,
                    "로그인 정보를 확인할 수 없습니다."
            );
        }

        try {
            boolean favorite = productFavoriteService.isFavoriteByLoginUser(
                    role,
                    loginId,
                    productId
            );

            return new ProductFavoriteResponseDto(
                    true,
                    favorite,
                    favorite ? "관심상품으로 등록된 상품입니다." : "관심상품이 아닙니다."
            );

        } catch (IllegalArgumentException e) {
            return new ProductFavoriteResponseDto(
                    false,
                    false,
                    e.getMessage()
            );
        }
    }
}