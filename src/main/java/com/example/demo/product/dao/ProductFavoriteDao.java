package com.example.demo.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.product.dto.request.ProductFavoriteRequestDto;
import com.example.demo.product.dto.response.ProductListResponseDto;

@Mapper
public interface ProductFavoriteDao {
//	이 사용자가 이 상품을 이미 관심상품으로 등록했는지 개수를 확인(0이면 관심 상품이 아니고 1이면 관심 상품임.
    int existsFavorite(ProductFavoriteRequestDto dto);
//	FAVORITE_PRODUCTS 테이블에 관심상품을 추가
    void insertFavorite(ProductFavoriteRequestDto dto);
//	이 사용자가 등록한 특정 상품 관심상품을 삭제한다.
    void deleteFavorite(ProductFavoriteRequestDto dto);
//	이 사용자의 관심상품 목록을 가져온다.
    List<ProductListResponseDto> selectFavoriteProducts(ProductFavoriteRequestDto dto);
//	개인 회원 로그인 아이디로 USERS.usr_no를 찾는 메서드.
    Long selectPersonalUsrNoByLoginId(String loginId);
//  기업 회원 로그인 아이디로 COMPANY_USERS.com_usr_no를 찾는 메서드
    Long selectCompanyUsrNoByLoginId(String loginId);
    
    
}
