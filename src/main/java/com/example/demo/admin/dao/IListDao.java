package com.example.demo.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.admin.dto.AdminDto;
import com.example.demo.admin.dto.ApprovalDto;
import com.example.demo.admin.dto.ProductDto;
import com.example.demo.personal.dto.UserDTO;

@Mapper
public interface IListDao {
	AdminDto getAdmin(@Param("admin_id") Long admin_id);
	List<AdminDto> getAdmins();
	UserDTO getUser(@Param("user_id") Long user_id);
	List<UserDTO> getUsers();
	// ApprovalDto getApproval(@Param("product_id") Long product_id);
	List<ApprovalDto> getApprovals();
	ApprovalDto getApproval(Long approval_id);
	void updateApproval(Long product_id, String status);
	void updateProduct(Long product_id, String status);
}
