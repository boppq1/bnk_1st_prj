package com.example.demo.admin.dao;



import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.admin.dto.ProductDto;

@Mapper
public interface IAdminProductDao {
//	void insertProduct(@Param("product_name") String product_name, @Param("is_active") String is_active, @Param("product_type") String product_type, @Param("min_period_month") Long min_period_month, @Param("max_period_month") Long max_period_month, @Param("min_amount") Long min_amount, @Param("max_amount") Long max_amount, @Param("target_customer") String target_customer, @Param("product_desc") String product_desc, @Param("interest_payment_type") String interest_payment_type, @Param("applied_exchange_rate_type") String applied_exchange_rate_type, @Param("created_by") String created_by);
//	ProductDto selectProduct(@Param("product_id") Long product_id);
//	List<ProductDto> getProducts();
//	void updateProduct(@Param("product_id") Long product_id, @Param("product_name") String product_name, @Param("is_active") String is_active, @Param("product_type") String product_type, @Param("min_period_month") Long min_period_month, @Param("max_period_month") Long max_period_month, @Param("min_amount") Long min_amount, @Param("max_amount") Long max_amount, @Param("target_customer") String target_customer, @Param("product_desc") String product_desc, @Param("interest_payment_type") String interest_payment_type, @Param("applied_exchange_rate_type") String applied_exchange_rate_type);

	int registerPro(ProductDto dto);
	List<ProductDto> listPro();
	ProductDto listDetail(Long product_id);
	List<ProductDto> selectByStatus(String approve_status);
	int updatePro(ProductDto dto);
	int updateProductStatus(ProductDto dto);
	int deletePro(Long product_id);
}
