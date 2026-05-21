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

    /*
     * 관심상품 관련 비즈니스 로직을 처리하는 Service
     *
     * Controller는 요청만 받고,
     * 실제 관심상품 등록/삭제 판단은 Service에서 처리함
     */
    private final ProductFavoriteService productFavoriteService;

    /*
     * JWT 토큰에서 로그인 사용자 정보를 꺼내기 위한 클래스
     *
     * 여기서는 accessToken 쿠키에서 꺼낸 token으로
     * role, login_id를 가져올 때 사용함
     */
    private final JwtUtil jwtUtil;


    /*
     * 하트 버튼 클릭 시 실행되는 API
     *
     * 예:
     * POST /api/products/favorites/11
     *
     * 의미:
     * 11번 상품을 관심상품으로 등록하거나,
     * 이미 등록되어 있으면 관심상품에서 삭제함
     */
    @PostMapping("/{productId}")
    public ProductFavoriteResponseDto toggleFavorite(
            /*
             * URL에 있는 상품 번호를 가져옴
             *
             * 예:
             * /api/products/favorites/11
             * 이면 productId = 11
             */
            @PathVariable Long productId,

            /*
             * 브라우저 쿠키에 저장된 accessToken을 가져옴
             *
             * required = false
             * - 쿠키가 없어도 에러를 바로 터뜨리지 않고 null로 받겠다는 뜻
             */
            @CookieValue(value = "accessToken", required = false) String token
    ) {

        /*
         * token이 없으면 로그인하지 않은 상태
         *
         * 이 경우 관심상품 등록/삭제를 하면 안 되므로
         * success = false로 응답함
         */
        if (token == null || token.isBlank()) {
            return new ProductFavoriteResponseDto(
                    false,
                    false,
                    "로그인이 필요합니다."
            );
        }

        /*
         * token이 있어도 만료되었거나 위조된 토큰일 수 있음
         *
         * jwtUtil.isValid(token)으로 유효성 검사
         */
        if (!jwtUtil.isValid(token)) {
            return new ProductFavoriteResponseDto(
                    false,
                    false,
                    "로그인 정보가 유효하지 않습니다."
            );
        }

        /*
         * JWT에서 role과 loginId를 꺼냄
         *
         * role 예:
         * ROLE_PERSONAL
         * ROLE_COMPANY
         *
         * loginId 예:
         * user01
         * cpuser01
         */
        String role = jwtUtil.getRole(token);
        String loginId = jwtUtil.getLoginId(token);

        /*
         * role 또는 loginId가 없으면
         * 어떤 사용자인지 알 수 없기 때문에 처리 중단
         */
        if (role == null || loginId == null) {
            return new ProductFavoriteResponseDto(
                    false,
                    false,
                    "로그인 정보를 확인할 수 없습니다."
            );
        }

        /*
         * 실제 관심상품 등록/삭제 처리는 Service에 맡김
         *
         * 여기서 Service는
         * role이 ROLE_PERSONAL이면 USERS에서 usr_no 조회
         * role이 ROLE_COMPANY면 COMPANY_USERS에서 com_usr_no 조회
         * 그 후 FAVORITE_PRODUCTS에 INSERT 또는 DELETE 처리함
         *
         * 반환값 favorite 의미:
         * true  -> 관심상품으로 등록된 상태
         * false -> 관심상품에서 삭제된 상태
         */
        boolean favorite = productFavoriteService.toggleFavoriteByLoginUser(
                role,
                loginId,
                productId
        );

        /*
         * JS로 결과 응답
         *
         * favorite이 true면 하트를 ♥로 바꾸면 되고
         * favorite이 false면 하트를 ♡로 바꾸면 됨
         */
        return new ProductFavoriteResponseDto(
                true,
                favorite,
                favorite ? "관심상품에 등록되었습니다." : "관심상품에서 삭제되었습니다."
        );
    }


    /*
     * 상품이 현재 관심상품인지 확인하는 API
     *
     * 예:
     * GET /api/products/favorites/11/status
     *
     * 의미:
     * 화면이 처음 로딩될 때
     * 11번 상품의 하트를 ♡로 보여줄지 ♥로 보여줄지 확인함
     */
    @GetMapping("/{productId}/status")
    public ProductFavoriteResponseDto favoriteStatus(
            /*
             * URL에서 상품 번호 가져오기
             */
            @PathVariable Long productId,

            /*
             * accessToken 쿠키 가져오기
             */
            @CookieValue(value = "accessToken", required = false) String token
    ) {

        /*
         * 로그인 안 된 상태
         *
         * 상태 조회에서는 alert를 띄우기보다
         * 그냥 favorite false로 응답하게 해도 됨
         */
        if (token == null || token.isBlank()) {
            return new ProductFavoriteResponseDto(
                    false,
                    false,
                    "로그인이 필요합니다."
            );
        }

        /*
         * 토큰 유효성 검사
         */
        if (!jwtUtil.isValid(token)) {
            return new ProductFavoriteResponseDto(
                    false,
                    false,
                    "로그인 정보가 유효하지 않습니다."
            );
        }

        /*
         * JWT에서 role과 loginId 꺼내기
         */
        String role = jwtUtil.getRole(token);
        String loginId = jwtUtil.getLoginId(token);

        /*
         * 로그인 정보가 부족하면 처리 불가
         */
        if (role == null || loginId == null) {
            return new ProductFavoriteResponseDto(
                    false,
                    false,
                    "로그인 정보를 확인할 수 없습니다."
            );
        }

        /*
         * 현재 상품이 관심상품인지 Service에서 확인
         *
         * true  -> 이미 관심상품
         * false -> 관심상품 아님
         */
        boolean favorite = productFavoriteService.isFavoriteByLoginUser(
                role,
                loginId,
                productId
        );

        /*
         * JS에 현재 관심상품 상태를 응답함
         */
        return new ProductFavoriteResponseDto(
                true,
                favorite,
                favorite ? "관심상품으로 등록된 상품입니다." : "관심상품이 아닙니다."
        );
    }
}