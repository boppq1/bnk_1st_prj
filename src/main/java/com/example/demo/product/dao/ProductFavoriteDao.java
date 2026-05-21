package com.example.demo.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.product.dto.request.ProductFavoriteRequestDto;
import com.example.demo.product.dto.response.ProductListResponseDto;

@Mapper
public interface ProductFavoriteDao {

    // 개인 회원 login_id로 USERS.usr_no 조회
    Long selectPersonalUsrNoByLoginId(@Param("loginId") String loginId);

    // 기업 회원 login_id로 COMPANY_USERS.com_usr_no 조회
    Long selectCompanyUsrNoByLoginId(@Param("loginId") String loginId);

    // 이미 관심상품인지 확인
    int existsFavorite(ProductFavoriteRequestDto dto);

    // 관심상품 추가
    void insertFavorite(ProductFavoriteRequestDto dto);

    // 관심상품 삭제
    void deleteFavorite(ProductFavoriteRequestDto dto);

    // 관심상품 목록 조회
    List<ProductListResponseDto> selectFavoriteProducts(ProductFavoriteRequestDto dto);
}