package com.example.demo.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.product.dao.ProductFavoriteDao;
import com.example.demo.product.dto.request.ProductFavoriteRequestDto;
import com.example.demo.product.dto.response.ProductListResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductFavoriteService {
    
    private final ProductFavoriteDao dao;

    /*
     * 하트 버튼을 눌렀을 때 실행되는 메서드
     *
     * Controller에서는 JWT에서 role, loginId만 꺼냄
     * 여기 Service에서 role, loginId를 이용해서
     * 실제 관심상품 테이블에 필요한 usrType, usrNo를 만들어줌
     */
    public boolean toggleFavoriteByLoginUser(String role, String loginId, Long productId) {

        /*
         * role + loginId + productId를 이용해서
         * FAVORITE_PRODUCTS 테이블에 필요한 DTO를 생성함
         */
        ProductFavoriteRequestDto dto = createFavoriteRequestDto(role, loginId, productId);

        /*
         * 이미 관심상품으로 등록되어 있는지 확인
         */
        int count = dao.existsFavorite(dto);

        /*
         * 이미 관심상품이면 삭제
         */
        if (count > 0) {
            dao.deleteFavorite(dto);
            return false;
        }

        /*
         * 관심상품이 아니면 추가
         */
        dao.insertFavorite(dto);
        return true;
    }

    /*
     * 이 상품이 현재 관심상품인지 확인하는 메서드
     *
     * 화면 처음 로딩될 때
     * 하트를 ♡로 보여줄지 ♥로 보여줄지 확인할 때 사용함
     */
    @Transactional(readOnly = true)
    public boolean isFavoriteByLoginUser(String role, String loginId, Long productId) {

        ProductFavoriteRequestDto dto = createFavoriteRequestDto(role, loginId, productId);

        return dao.existsFavorite(dto) > 0;
    }

    /*
     * 관심상품 목록을 가져오는 메서드
     *
     * /product/favorite 화면에서 사용함
     */
    @Transactional(readOnly = true)
    public List<ProductListResponseDto> getFavoriteProductsByLoginUser(String role, String loginId) {

        /*
         * 목록 조회에서는 특정 상품 번호가 필요 없기 때문에
         * productId는 null로 넣음
         */
        ProductFavoriteRequestDto dto = createFavoriteRequestDto(role, loginId, null);

        return dao.selectFavoriteProducts(dto);
    }

    /*
     * role과 loginId를 이용해서
     * 관심상품 테이블에 필요한 usrType, usrNo를 만드는 공통 메서드
     *
     * 이 메서드는 Service 내부에서만 쓰기 때문에 private으로 둠
     */
    private ProductFavoriteRequestDto createFavoriteRequestDto(
            String role,
            String loginId,
            Long productId
    ) {
        String usrType;
        Long usrNo;

        if ("user".equals(role) || "ROLE_PERSONAL".equals(role)) {
            usrType = "PERSONAL";
            usrNo = dao.selectPersonalUsrNoByLoginId(loginId);
        }

        else if ("company".equals(role) || "ROLE_COMPANY".equals(role)) {
            usrType = "COMPANY";
            usrNo = dao.selectCompanyUsrNoByLoginId(loginId);
        }

        else {
            throw new IllegalArgumentException(
                    "지원하지 않는 사용자 권한입니다. role=" + role
            );
        }

        if (usrNo == null) {
            throw new IllegalArgumentException(
                    "사용자 정보를 찾을 수 없습니다. role=" + role + ", loginId=" + loginId
            );
        }

        return new ProductFavoriteRequestDto(usrType, usrNo, productId);
    }
}