package com.example.demo.admin.controller;

import com.example.demo.admin.dao.IListDao;
import com.example.demo.admin.service.AdminProductService;
import com.example.demo.admin.service.AdminService;
import com.example.demo.product.dto.request.ProductListRequestDto;
import com.example.demo.search.SearchDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class headController {

    private final AdminService serv;
    private final AdminProductService proServ;
    private final SearchDao searchDao;
    private final IListDao iListDao;

    @GetMapping("/admin/headDashboard/data")
    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();

        // 준비된 함수들을 호출해서 데이터 넣기
        data.put("totalKeywordCount", searchDao.selectAll().size());
        data.put("personalRecCount", serv.getPersonalSuggestKeyword());
        data.put("corpRecCount", serv.getCompanySuggestKeyword());
        data.put("totalProductCount", proServ.getTotalCount());
        data.put("userCount", iListDao.getUsers().size());
        data.put("companyCount", iListDao.getCompanies().size());

        return data; // JSON 형태로 자동 변환됨
    }
}
