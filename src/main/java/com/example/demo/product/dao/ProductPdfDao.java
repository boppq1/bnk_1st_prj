package com.example.demo.product.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.product.dto.ProductPdfDto;

@Mapper
public interface ProductPdfDao {
	ProductPdfDto findByProductId(Long productId);
}
