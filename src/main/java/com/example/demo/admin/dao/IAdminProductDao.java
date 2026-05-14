package com.example.demo.admin.dao;



import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.admin.dto.ProductDto;

@Mapper
public interface IAdminProductDao {
	int registerPro(ProductDto dto);
	List<ProductDto> listPro();
	ProductDto listDetail(Long product_id);
	List<ProductDto> selectByStatus(String approve_status);
	int updatePro(ProductDto dto);
	int updateProductStatus(ProductDto dto);
	int deletePro(Long product_id);
}
