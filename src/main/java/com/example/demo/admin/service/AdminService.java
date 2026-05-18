package com.example.demo.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.admin.dao.IListDao;
import com.example.demo.admin.dao.IAdminExchangeDao;
import com.example.demo.admin.dao.IAdminLogDao;
import com.example.demo.admin.dao.IAdminProductDao;
import com.example.demo.admin.dto.AdminActionLogDto;
import com.example.demo.admin.dto.AdminDto;
import com.example.demo.admin.dto.ApiLogDto;
import com.example.demo.admin.dto.ApprovalDto;
import com.example.demo.admin.dto.CompaniesDto;
import com.example.demo.admin.dto.ExchangeRequestDto;
import com.example.demo.admin.dto.KeywordBanDto;
import com.example.demo.admin.dto.ProductApprovalDto;
import com.example.demo.admin.dto.ProductDto;
import com.example.demo.admin.dto.SearchLogDto;
import com.example.demo.admin.dto.SuggestedSearchDto;
import com.example.demo.company.dto.AccountsCompanyDTO;
import com.example.demo.company.dto.CompanyUserDTO;
import com.example.demo.company.dto.ForeignAccountsCompanyDTO;
import com.example.demo.personal.dto.Accounts_personalDTO;
import com.example.demo.personal.dto.Foreign_accounts_personal;
import com.example.demo.personal.dto.UserDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	final IAdminProductDao productDao;
	final IListDao listDao;
	final IAdminLogDao logDao;
	final IAdminExchangeDao exchangeDao;
	
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
	
	public CompanyUserDTO getCompanyUser(Long company_user_id) {
		return listDao.getCompanyUser(company_user_id);
	}
	
	public List<CompanyUserDTO> getCompanyUserList() {
		return listDao.getCompanyUsers();
	}
	
	public void updateAdmin(AdminDto dto) {
		listDao.updateAdmin(dto.getAdmin_id(), dto.getPassword(), dto.getDepartment(), dto.getAdmin_pw(), dto.getName(), dto.getAdmin_role());
	}
	
	public void updateUser(UserDTO dto) {
		listDao.updateUser(dto.getUser_id(), dto.getPassword(), dto.getName(), dto.getPhone(), dto.getBirth(), dto.getEmail(), dto.getGender(), dto.getE_name(), dto.getPostal_code(), dto.getAddress(), dto.getAddress_detail());
	}
	
	public void updateCompanyUser(CompanyUserDTO dto) {
		listDao.updateCompanyUser(dto);
	}
	
	public List<ProductApprovalDto> getApproval(Long product_id) {
		return listDao.getApproval(product_id);
	}
	
	public List<ApprovalDto> getApprovals() {
		return listDao.getApprovals();
	}
	
	// 세션에서 관리자 아이디 받아서 넣어야함
	public boolean approvedStatus(Long product_id, String status) {
		listDao.updateApproval(product_id, status, "admin_01");
		listDao.updateProduct(product_id, status, "admin_01");
		return true;
	}
	
	public List<AdminActionLogDto> adminLog() {
		return logDao.adminLog();
	}
	
	public List<ApiLogDto> userLog() {
		return logDao.userLog();
	}
	
	public List<ExchangeRequestDto> exchangeList() {
		return exchangeDao.exchangeList();
	}
	
	public List<CompaniesDto> getCompanies() {
		return listDao.getCompanies();
	}
	
	public List<AccountsCompanyDTO> getCompanyDomesticAccount(Long company_id) {
		return listDao.getCompanyDomesticAccount(company_id);
	}
	
	public List<ForeignAccountsCompanyDTO> getCompanyForeignAccount(Long company_id) {
		return listDao.getCompanyForeignAccount(company_id);
	}
	
	public List<Accounts_personalDTO> getPersonalDomesticAccount(Long user_id) {
		return listDao.getPersonalDomesticAccount(user_id);
	}
	
	public List<Foreign_accounts_personal> getPersonalForeignAccount(Long user_id) {
		return listDao.getPersonalForeignAccount(user_id);
	}
	
	public List<SearchLogDto> getSearchPersonal() {
		return listDao.getSearchPersonalLog();
	}
	
	public List<SuggestedSearchDto> getSuggestPersonal() {
		return listDao.getSuggestPersonalSearch();
	}
	
	public List<SearchLogDto> getSearchCompany() {
		return listDao.getSearchCompanyLog();
	}
	
	public List<SuggestedSearchDto> getSuggestCompany() {
		return listDao.getSuggestCompanySearch();
	}
	
	public List<KeywordBanDto> getKeywordBanList() {
		return listDao.getKeywordBanList();
	}
	
	public void keywordBan(String keyword, Long adm_no) {
		listDao.keywordBan(keyword, adm_no);
	}
	
	public boolean checkKeywordExist(String keyword) {
		
		if(listDao.getKeywordBan(keyword) != null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void deleteBanKeyword(String keyword) {
		listDao.deleteBanKeyword(keyword);
	}
	
	public void setSuggestKeyword(String keyword, String role, Long search_volume) {
		listDao.setSuggestKeyword(keyword, role, search_volume);
	}
	
	public SearchLogDto getCompanySearchLog(String keyword) {
		return listDao.getKeyword(keyword);
	}
	
	public SearchLogDto getPersonalSearchLog(String keyword) {
		return listDao.getKeyword(keyword);
	}
	
	public SuggestedSearchDto getCompanySuggestKeyword(String keyword) {
		return listDao.getCompanySuggestKeyword(keyword);
	}
	
	public SuggestedSearchDto getPersonalSuggestKeyword(String keyword) {
		return listDao.getPersonalSuggestKeyword(keyword);
	}
	
	public int getCompanySuggestKeyword() {
		return listDao.getCompanySuggestKeywords().size();
	}
	
	public int getPersonalSuggestKeyword() {
		return listDao.getPersonalSuggestKeywords().size();
	}
	
	public void deletePersonalSuggestKeyword() {
		listDao.deletePersonalSuggestKeyword();
	}
	
	public void deleteCompanySuggestKeyword() {
		listDao.deleteCompanySuggestKeyword();
	}
	
	public void updateSuggestKeyword() {
		listDao.truncateSuggestSearch();
		listDao.updateSuggestKeyword();
	}
	
}

