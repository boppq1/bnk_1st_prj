package com.example.demo.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.admin.dto.AdminDto;
import com.example.demo.admin.dto.ApprovalDto;
import com.example.demo.company.dto.CompanyUserDTO;
import com.example.demo.personal.dto.UserDTO;

@Mapper
public interface IListDao {
	AdminDto getAdmin(@Param("admin_id") Long admin_id);
	List<AdminDto> getAdmins();
	UserDTO getUser(@Param("user_id") Long user_id);
	List<UserDTO> getUsers();
	// ApprovalDto getApproval(@Param("product_id") Long product_id);
	CompanyUserDTO getCompanyUser(@Param("company_user_id") Long company_user_id);
	List<CompanyUserDTO> getCompanyUsers();
	List<ApprovalDto> getApprovals();
	ApprovalDto getApproval(Long approval_id);
	void updateApproval(@Param("product_id") Long product_id, @Param("status")String status, @Param("admin_id") String admin_id);
	void updateProduct(@Param("product_id") Long product_id, @Param("status") String status, @Param("admin_id") String admin_id);
	void updateAdmin(@Param("admin_id") Long admin_id, @Param("password") String password, @Param("department") String department, @Param("admin_pw") String admin_pw, @Param("name") String name, @Param("admin_role") String admin_role);
	void updateUser(@Param("user_id") Long user_id, @Param("password") String password, @Param("name") String name, @Param("phone") String phone, @Param("birth") String birth, @Param("email") String email, @Param("gender") String gender, @Param("e_name") String e_name, @Param("postal_code") String postal_code, @Param("address") String address, @Param("address_detail") String address_detail);
	void updateCompanyUser(CompanyUserDTO dto);
}
