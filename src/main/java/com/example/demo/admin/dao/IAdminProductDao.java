package com.example.demo.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.admin.dto.ProductDto;

@Mapper
public interface IAdminProductDao {

	// 상품 등록
	int insertPro(ProductDto dto);

	// 현재 상품 번호 조회
	Long selectCurrentProductNo();

	// PDF 등록
	int insertProPdf(ProductDto dto);

	// 신청 등록
	int insertProRequest(ProductDto dto);

	// 상품 목록
	List<ProductDto> listPro();

	// 상품 상세
	ProductDto listDetail(Long product_no);

	// 상태별 조회
	List<ProductDto> selectByStatus(String approve_status);

	// 상품 수정
	int updatePro(ProductDto dto);

	// PDF 수정
	int updateProPdf(ProductDto dto);

	// 판매 상태 변경
	int updateProductStatus(ProductDto dto);

	// PDF 삭제
	int deleteProductPdf(Long product_no);

	// 신청 삭제
	int deleteProductRequest(Long product_no);

	// 상품 삭제
	int deletePro(Long product_no);

	int saveProduct(ProductDto dto);
	int submitProduct(ProductDto dto);

}