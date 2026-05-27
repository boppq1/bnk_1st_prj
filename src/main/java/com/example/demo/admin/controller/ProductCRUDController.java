package com.example.demo.admin.controller;

import com.example.demo.admin.dao.IAdminProductDao;
import com.example.demo.admin.dao.IListDao;
import com.example.demo.admin.dto.*;
import com.example.demo.admin.service.AdminMergeService;
import com.example.demo.admin.service.AdminProductService;
import com.example.demo.config.FileProperties;

import com.example.demo.interceptor.AdminLog;
import com.example.demo.jwt.JwtAuthFilter;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.product.dao.ProductDao;
import com.example.demo.search.SearchDao;
import com.example.demo.search.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ProductCRUDController {

    private final AdminProductService serv;
    private final AdminMergeService adminServ;
    private final FileProperties fileProperties;
    private List<InterestRateDto> rates;
    private final JwtUtil jwt;
    private final JwtAuthFilter jwtFilter;
    private final AdminMergeService mergeServ;
    private final IListDao iListDao;
    private final SearchDao searchDao;
    private final IAdminProductDao productDao;

    // ----------------------------------------------------------------
    // 상품 등록 페이지
    // ----------------------------------------------------------------
    @GetMapping("/admin/productRegisterPage")
    public String registerPage(HttpServletRequest request, Model model,
                               @CookieValue(value = "accessToken") String token) {
        String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
        if (dto == null) return "redirect:/adminLogin?error=true";
        model.addAttribute("admin", dto);
        model.addAttribute("product", new ProductDto());
        return "admin/productRegisterPage";
    }

    // ----------------------------------------------------------------
    // 상품 등록
    //
    // 통화 처리 방식:
    //   HTML 폼에서 <input name="currencies[0].cur_cd" ...>
    //              <input name="currencies[0].cur_nm" ...> 형태로 전송하면
    //   Spring이 ProductDto.currencies 리스트에 자동 바인딩합니다.
    //   또는 JS로 체크박스 체크 여부를 hidden input에 담아도 됩니다.
    // ----------------------------------------------------------------
    @AdminLog(action = "관리자 상품 등록")
    @PostMapping("/admin/productRegister")
    public String registerProduct(
            ProductDto dto,
            @RequestParam("basicTermsFile")    MultipartFile basicTermsFile,
            @RequestParam("categoryTermsFile") MultipartFile categoryTermsFile,
            @RequestParam("specialTermsFile")  MultipartFile specialTermsFile,
            @RequestParam("productGuideFile")  MultipartFile productGuideFile,
            HttpServletRequest request, Model model,
            @CookieValue(value = "accessToken") String token) {

        String id = jwt.getLoginId(token);
        AdminDto admDto = mergeServ.selectMyPage(id);
        if (dto == null) return "redirect:/adminLogin?error=true";
        model.addAttribute("admin", admDto);

        dto.setCreated_by(admDto.getName());
        dto.setRequester_id(admDto.getName());
        dto.setBasic_terms_path(saveFile(basicTermsFile));
        dto.setCategory_terms_path(saveFile(categoryTermsFile));
        dto.setSpecial_terms_path(saveFile(specialTermsFile));
        dto.setProduct_guide_path(saveFile(productGuideFile));

        // dto.getCurrencies()는 폼 바인딩으로 자동 채워집니다.
        serv.registerProduct(dto);

        return "redirect:/admin/productListPage";
    }

    // ----------------------------------------------------------------
    // 공통 파일 저장
    // ----------------------------------------------------------------
    private String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            String uploadDir = fileProperties.getUploadDir();
            String fileName  = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + fileName);
            java.nio.file.Files.createDirectories(path.getParent());
            file.transferTo(path.toFile());
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    // ----------------------------------------------------------------
    // 상품 목록 조회
    // ----------------------------------------------------------------
    @GetMapping("/admin/productListPage")
    public String listPro(
            HttpServletRequest request,
            Model model,
            @CookieValue(value = "accessToken") String token,
            @RequestParam(value = "page",    defaultValue = "1") int page,
            @RequestParam(value = "keyword", required = false)   String keyword,
            @RequestParam(value = "type",    required = false)   String type,
            @RequestParam(value = "status",  required = false)   String status) {

        String id   = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
        if (dto == null) return "redirect:/adminLogin?error=true";
        model.addAttribute("admin", dto);

        int pageSize = 7;
        int offset   = (page - 1) * pageSize;

        Map<String, Object> param = new HashMap<>();
        param.put("offset",   offset);
        param.put("pageSize", pageSize);
        param.put("keyword",  keyword);
        param.put("type",     type);
        param.put("status",   status);

        List<ProductDto> products = serv.searchProducts(param);
        int totalCount  = serv.searchProductCount(param);
        int totalPage   = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("products",     products);
        model.addAttribute("totalCount",   totalCount);
        model.addAttribute("totalPage",    totalPage);
        model.addAttribute("currentPage",  page);
        model.addAttribute("keyword",      keyword);
        model.addAttribute("type",         type);
        model.addAttribute("status",       status);
        model.addAttribute("depositCount", serv.getCountByType("DEPOSIT"));
        model.addAttribute("savingCount",  serv.getCountByType("SAVING"));
        model.addAttribute("pendingCount", serv.getCountByStatus("검토중"));

        return "admin/productListPage";
    }

    // ----------------------------------------------------------------
    // 상태별 조회
    // ----------------------------------------------------------------
    @GetMapping("/admin/proStatus")
    public String selectByStatus(@RequestParam("approve_status") String approve_status,
                                 HttpServletRequest request, Model model,
                                 @CookieValue(value = "accessToken") String token) {
        String id   = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
        if (dto == null) return "redirect:/adminLogin?error=true";
        model.addAttribute("admin", dto);

        List<ProductDto> products = serv.getProductByStatus(approve_status);
        model.addAttribute("products",   products);
        model.addAttribute("totalCount", products.size());

        return "admin/productListPage";
    }

    // ----------------------------------------------------------------
    // 상품 상태 변경 (AJAX)
    // ----------------------------------------------------------------
    @PostMapping("/admin/status/update")
    @ResponseBody
    public ResponseEntity<String> updateStatus(
            @RequestParam("product_id")     Long product_id,
            @RequestParam("approve_status") String approve_status,
            @CookieValue("accessToken")     String token) {

        AdminDto adminDto = mergeServ.selectMyPage(jwt.getLoginId(token));
        ProductDto dto = new ProductDto();
        dto.setProduct_id(product_id);
        dto.setApprove_status(approve_status);
        dto.setUpdated_by(adminDto.getLogin_id());
        serv.updateProductStatus(dto);
        return ResponseEntity.ok("ok");
    }

    // ----------------------------------------------------------------
    // 상품 상세 조회 (AJAX) — 통화 목록 포함됩니다 (resultMap 또는 서비스에서 채워줌)
    // ----------------------------------------------------------------
    @ResponseBody
    @GetMapping("/admin/product/detail/{product_id}")
    public ProductDto productDetail(@PathVariable("product_id") Long product_id,
                                    HttpServletRequest request, Model model,
                                    @CookieValue(value = "accessToken") String token) {
        return serv.getProductDetail(product_id);
    }

    // ----------------------------------------------------------------
    // 상품 수정 페이지
    // ----------------------------------------------------------------
    @GetMapping("/admin/updatePro/{product_id}")
    public String updatePage(@PathVariable("product_id") Long product_id,
                             HttpServletRequest request, Model model,
                             @CookieValue(value = "accessToken") String token) {
        String id   = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
        if (dto == null) return "redirect:/adminLogin?error=true";
        model.addAttribute("admin", dto);

        ProductDto proDto = serv.getProductDetail(product_id);
        model.addAttribute("product", proDto);
        return "admin/productUpdatePage";
    }

    // ----------------------------------------------------------------
    // 상품 수정 (저장 or 제출)
    //
    // 통화: 수정 페이지에서도 currencies[0].cur_cd / currencies[0].cur_nm 으로
    //       체크된 통화만 폼에 실어 보내면 service 계층이 기존 데이터를
    //       전부 지우고 새로 insert 합니다.
    // ----------------------------------------------------------------
    @AdminLog(action = "관리자 상품 수정")
    @PostMapping("/admin/updateProWrite")
    public String updateProduct(
            ProductDto dto,
            @RequestParam("action") String action,
            @RequestParam(value = "basicTermsFile",    required = false) MultipartFile basicTermsFile,
            @RequestParam(value = "categoryTermsFile", required = false) MultipartFile categoryTermsFile,
            @RequestParam(value = "specialTermsFile",  required = false) MultipartFile specialTermsFile,
            @RequestParam(value = "productGuideFile",  required = false) MultipartFile productGuideFile,
            HttpServletRequest request, Model model,
            @CookieValue(value = "accessToken") String token) {

        String id       = jwt.getLoginId(token);
        AdminDto adminDto = mergeServ.selectMyPage(id);
        if (dto == null) return "redirect:/adminLogin?error=true";
        model.addAttribute("admin", dto);

        // ProductCRUDController.java 의 updateProduct 메서드 내부

        String basicPath = saveFile(basicTermsFile);
        if (basicPath != null) dto.setBasic_terms_path(basicPath);

        String categoryPath = saveFile(categoryTermsFile);
        if (categoryPath != null) dto.setCategory_terms_path(categoryPath);

        String specialPath = saveFile(specialTermsFile);
        if (specialPath != null) dto.setSpecial_terms_path(specialPath);

        String guidePath = saveFile(productGuideFile);
        if (guidePath != null) dto.setProduct_guide_path(guidePath);

        dto.setUpdated_by(adminDto.getLogin_id());

        // dto.getCurrencies()는 폼 바인딩으로 자동 채워집니다.
        if ("save".equals(action)) {
            serv.saveProduct(dto);
        } else if ("submit".equals(action)) {
            serv.submitProduct(dto);
        }

        return "redirect:/admin/productListPage";
    }

    // ----------------------------------------------------------------
    // 상품 삭제 (통화도 함께 삭제 — service에서 처리)
    // ----------------------------------------------------------------
    @AdminLog(action = "관리자 상품 삭제")
    @GetMapping("/deletePro/{product_id}")
    public String deletePro(@PathVariable("product_id") Long product_id) {
        serv.deleteProduct(product_id);
        return "redirect:/admin/productListPage";
    }

    // ----------------------------------------------------------------
    // 대시보드
    // ----------------------------------------------------------------
    @GetMapping("/admin/headDashboard")
    public String headDashboard(HttpServletRequest request, Model model,
                                @CookieValue(value = "accessToken", required = false) String token) {

        if (token == null) return "redirect:/adminLogin";
        String id    = jwt.getLoginId(token);
        AdminDto admin = mergeServ.selectMyPage(id);
        if (admin == null) return "redirect:/adminLogin?error=true";

        Map<String, Object> dashboard = new HashMap<>();

        List<SuggestedSearchDto> personal = iListDao.getSuggestPersonalSearch();
        List<SuggestedSearchDto> corp     = iListDao.getSuggestCompanySearch();

        List<SuggestedSearchDto> allSuggest = new ArrayList<>();
        allSuggest.addAll(personal);
        allSuggest.addAll(corp);
        allSuggest.sort((a, b) -> Long.compare(b.getSearch_volume(), a.getSearch_volume()));

        long maxVolume = allSuggest.isEmpty() ? 1 : allSuggest.get(0).getSearch_volume();

        List<Map<String, Object>> topKeywords = allSuggest.stream()
                .limit(5)
                .map(s -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("keyword",      s.getKeyword());
                    map.put("searchVolume", s.getSearch_volume());
                    map.put("barPercent",   (s.getSearch_volume() * 100) / maxVolume);
                    return map;
                })
                .collect(Collectors.toList());

        dashboard.put("totalKeywordCount",
                iListDao.getSearchPersonalLog().size() + iListDao.getSearchCompanyLog().size());
        dashboard.put("topKeywords", topKeywords);
        dashboard.put("personalRecommends",
                personal.stream().map(SuggestedSearchDto::getKeyword).collect(Collectors.toList()));
        dashboard.put("corpRecommends",
                corp.stream().map(SuggestedSearchDto::getKeyword).collect(Collectors.toList()));
        dashboard.put("recommendKeywordCount", allSuggest.size());
        dashboard.put("personalRecCount",       personal.size());
        dashboard.put("corpRecCount",           corp.size());

        List<ProductDto> productList = productDao.listPro(0, 10);
        dashboard.put("products",          productList);
        dashboard.put("totalProductCount", productDao.getTotalCount());

        dashboard.put("userList",    iListDao.getUsers());
        dashboard.put("companyList", iListDao.getCompanies());

        model.addAttribute("admin",     admin);
        model.addAttribute("dashboard", dashboard);

        return "admin/headDashboard";
    }
}