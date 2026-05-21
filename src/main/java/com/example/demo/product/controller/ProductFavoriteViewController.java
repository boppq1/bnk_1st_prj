package com.example.demo.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.product.service.ProductFavoriteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductFavoriteViewController {

    private final ProductFavoriteService productFavoriteService;
    private final JwtUtil jwtUtil;

    /*
     * 관심상품 목록 페이지
     *
     * 주소:
     * /product/favorite
     *
     * 역할:
     * 로그인한 사용자의 관심상품 목록을 조회해서
     * product/productFavorite.html 화면으로 전달함
     */
    @GetMapping("/product/favorite")
    public String favoriteProducts(
            Model model,
            @CookieValue(value = "accessToken", required = false) String token
    ) {

        /*
         * 토큰이 없으면 로그인 페이지로 이동
         *
         * 너희 로그인 페이지 주소는 /loginPage임
         * 그래서 /login 말고 /loginPage로 보내야 함
         */
        if (token == null || token.isBlank()) {
            return "redirect:/loginPage";
        }

        /*
         * 토큰이 만료되었거나 잘못된 경우도 로그인 페이지로 이동
         */
        if (!jwtUtil.isValid(token)) {
            return "redirect:/loginPage";
        }

        /*
         * JWT에서 role과 loginId를 꺼냄
         *
         * role:
         * user 또는 company
         *
         * loginId:
         * 로그인 아이디
         */
        String role = jwtUtil.getRole(token);
        String loginId = jwtUtil.getLoginId(token);

        if (role == null || loginId == null) {
            return "redirect:/loginPage";
        }

        /*
         * 관심상품 목록 조회 후 화면으로 전달
         *
         * HTML에서는 ${favorites}로 사용 가능
         */
        model.addAttribute(
                "favorites",
                productFavoriteService.getFavoriteProductsByLoginUser(role, loginId)
        );

        return "product/productFavorite";
    }
}