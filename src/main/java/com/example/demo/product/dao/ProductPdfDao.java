package com.example.demo.product.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.product.dto.ProductPdfDto;

import java.util.List;

@Mapper
public interface ProductPdfDao {
	//ProductPdfDto findByProductId(Long productId);
	List<ProductPdfDto> findByProductId(Long productId);
}
