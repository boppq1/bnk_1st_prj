package com.example.demo.admin.service;





import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.admin.dao.IListDao;
import com.example.demo.admin.dao.IAdminProductDao;
import com.example.demo.admin.dto.AdminDto;
import com.example.demo.admin.dto.ProductDto;
import com.example.demo.personal.dto.UserDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	final IAdminProductDao productDao;
	final IListDao listDao;
	
	public boolean makeProduct(ProductDto dto) {
		dto.setCreated_by("admin");
		productDao.insertProduct(dto.getProduct_name(), dto.getIs_active(), dto.getProduct_type(), dto.getMin_period_month(), dto.getMax_period_month(), dto.getMin_amount(), dto.getMax_amount(), dto.getTarget_customer(), dto.getProduct_desc(), dto.getInterest_payment_type(), dto.getApplied_exchange_rate_type(), dto.getCreated_by());
		return true;
	}
	
	public ProductDto getProduct(Long product_id) {
		return productDao.getProduct(product_id);
	}
	
	public List<ProductDto> getProducts() {
		return productDao.getProducts();
	}
	
	public AdminDto getAdmin(Long admin_id) {
		return listDao.getAdmin(admin_id);
	}
	
	public List<AdminDto> getAdminList() {
		return listDao.getAdmins();
	}
	
	public UserDTO getUser(Long user_id) {
		return listDao.getUser(user_id);
	}
	
	public List<UserDTO> getUserList() {
		return listDao.getUsers();
	}
	
}

