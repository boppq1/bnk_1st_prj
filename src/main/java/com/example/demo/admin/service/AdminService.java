package com.example.demo.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.admin.dao.IProductDao;
import com.example.demo.admin.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	final IProductDao dao;
	
	public boolean makeProduct(ProductDto dto) {
		dto.setCreated_by("admin");
		dao.insertProduct(dto.getProduct_name(), dto.getIs_active(), dto.getProduct_type(), dto.getMin_period_month(), dto.getMax_period_month(), dto.getMin_amount(), dto.getMax_amount(), dto.getTarget_customer(), dto.getProduct_desc(), dto.getInterest_payment_type(), dto.getApplied_exchange_rate_type(), dto.getCreated_by());
		return true;
	}
	
	public ProductDto getProduct(Long product_id) {
		return dao.getProduct(product_id);
	}
	
	public List<ProductDto> getProducts() {
		return dao.getProducts();
	}
}
