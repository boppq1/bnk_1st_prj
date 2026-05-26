package com.example.demo.admin.controller;

import com.example.demo.admin.dto.AdminDto;
import com.example.demo.admin.dto.InterestRateDto;
import com.example.demo.admin.dto.ProductDto;
import com.example.demo.admin.service.AdminMergeService;
import com.example.demo.admin.service.AdminProductService;
import com.example.demo.config.FileProperties;

import com.example.demo.interceptor.JwtFilter;
import com.example.demo.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductCRUDController {

    private final AdminProductService serv;
    private final AdminMergeService adminServ;
    private final FileProperties fileProperties;
    private List<InterestRateDto> rates;
    private final JwtUtil jwt;
    private final JwtFilter jwtFilter;
    private final AdminMergeService mergeServ;

    // 상품 등록 페이지
    @GetMapping("/admin/productRegisterPage")
    public String registerPage(HttpServletRequest request, Model model, @CookieValue(value = "accessToken") String token) {
        String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
        if (dto == null) return "redirect:/adminLogin?error=true";
        model.addAttribute("admin", dto);
        model.addAttribute("product", new ProductDto());
        return "admin/productRegisterPage";
    }

    // 상품 등록
    @PostMapping("/admin/productRegister")
    public String registerProduct(
            ProductDto dto,
            @RequestParam("basicTermsFile") MultipartFile basicTermsFile,
            @RequestParam("categoryTermsFile") MultipartFile categoryTermsFile,
            @RequestParam("specialTermsFile") MultipartFile specialTermsFile,
            @RequestParam("productGuideFile") MultipartFile productGuideFile,
            HttpServletRequest request, Model model, @CookieValue(value = "accessToken") String token) {

        String id = jwt.getLoginId(token);
        AdminDto admDto = mergeServ.selectMyPage(id);
        if (dto == null) return "redirect:/adminLogin?error=true";
        model.addAttribute("admin", dto);

        dto.setCreated_by(admDto.getName());
        dto.setRequester_id(admDto.getName());
        dto.setBasic_terms_path(saveFile(basicTermsFile));
        dto.setCategory_terms_path(saveFile(categoryTermsFile));
        dto.setSpecial_terms_path(saveFile(specialTermsFile));
        dto.setProduct_guide_path(saveFile(productGuideFile));
        serv.registerProduct(dto);

        return "redirect:/admin/productListPage";
    }

    // 공통 파일 저장 메서드
    private String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            String uploadDir = fileProperties.getUploadDir();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + fileName);
            java.nio.file.Files.createDirectories(path.getParent());
            file.transferTo(path.toFile());
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    // 상품 목록 조회
    @GetMapping("/admin/productListPage")
    public String listPro(HttpServletRequest request, Model model, @CookieValue(value = "accessToken") String token,
                          @RequestParam(value = "page", defaultValue = "1") int page) {
        String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
        if (dto == null) return "redirect:/adminLogin?error=true";
        model.addAttribute("admin", dto);

        int pageSize = 7;
        int offset = (page - 1) * pageSize;
        List<ProductDto> products = serv.getProductList(offset, pageSize);
        int totalCount = serv.getTotalCount();
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("products", products);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("depositCount", serv.getCountByType("DEPOSIT"));
        model.addAttribute("savingCount",  serv.getCountByType("SAVING"));
        model.addAttribute("pendingCount", serv.getCountByStatus("검토중"));

        return "admin/productListPage";
    }

    // 상태별 조회
    @GetMapping("/admin/proStatus")
    public String selectByStatus(@RequestParam("approve_status") String approve_status,
                                 HttpServletRequest request, Model model, @CookieValue(value = "accessToken") String token) {
        String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
        if (dto == null) return "redirect:/adminLogin?error=true";
        model.addAttribute("admin", dto);

        List<ProductDto> products = serv.getProductByStatus(approve_status);
        model.addAttribute("products", products);
        model.addAttribute("totalCount", products.size());

        return "admin/productListPage";
    }

    // ★ 상품 상태 변경 (AJAX) — 기존 중복 메서드 제거 후 이것만 유지
    @PostMapping("/admin/status/update")
    @ResponseBody
    public ResponseEntity<String> updateStatus(
            @RequestParam("product_id") Long product_id,
            @RequestParam("approve_status") String approve_status,
            @CookieValue("accessToken") String token) {

        AdminDto adminDto = mergeServ.selectMyPage(jwt.getLoginId(token));
        ProductDto dto = new ProductDto();
        dto.setProduct_id(product_id);
        dto.setApprove_status(approve_status);
        dto.setUpdated_by(adminDto.getLogin_id());
        serv.updateProductStatus(dto);
        return ResponseEntity.ok("ok");
    }

    // 상품 상세 조회 (AJAX)
    @ResponseBody
    @GetMapping("/admin/product/detail/{product_id}")
    public ProductDto productDetail(@PathVariable("product_id") Long product_id,
                                    HttpServletRequest request, Model model,
                                    @CookieValue(value = "accessToken") String token) {
        return serv.getProductDetail(product_id);
    }

    // 상품 수정 페이지
    @GetMapping("/admin/updatePro/{product_id}")
    public String updatePage(@PathVariable("product_id") Long product_id,
                             HttpServletRequest request, Model model, @CookieValue(value = "accessToken") String token) {
        String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
        if (dto == null) return "redirect:/adminLogin?error=true";
        model.addAttribute("admin", dto);

        ProductDto proDto = serv.getProductDetail(product_id);
        model.addAttribute("product", proDto);
        return "admin/productUpdatePage";
    }

    // 수정
    @PostMapping("/admin/updateProWrite")
    public String updateProduct(
            ProductDto dto,
            @RequestParam("action") String action,
            @RequestParam(value = "basicTermsFile",    required = false) MultipartFile basicTermsFile,
            @RequestParam(value = "categoryTermsFile", required = false) MultipartFile categoryTermsFile,
            @RequestParam(value = "specialTermsFile",  required = false) MultipartFile specialTermsFile,
            @RequestParam(value = "productGuideFile",  required = false) MultipartFile productGuideFile,
            HttpServletRequest request, Model model, @CookieValue(value = "accessToken") String token) {

        String id = jwt.getLoginId(token);
        AdminDto adminDto = mergeServ.selectMyPage(id);
        if (dto == null) return "redirect:/adminLogin?error=true";
        model.addAttribute("admin", dto);

        dto.setBasic_terms_path(saveFile(basicTermsFile));
        dto.setCategory_terms_path(saveFile(categoryTermsFile));
        dto.setSpecial_terms_path(saveFile(specialTermsFile));
        dto.setProduct_guide_path(saveFile(productGuideFile));
        dto.setUpdated_by(adminDto.getLogin_id());

        if ("save".equals(action)) {
            serv.saveProduct(dto);
        } else if ("submit".equals(action)) {
            serv.submitProduct(dto);
        }

        return "redirect:/admin/productListPage";
    }

    // 상품 삭제
    @GetMapping("/deletePro/{product_id}")
    public String deletePro(@PathVariable("product_id") Long product_id) {
        serv.deleteProduct(product_id);
        return "redirect:/admin/productListPage";
    }

    // 대쉬보드
    @GetMapping("/admin/headDashboard")
    public String headDashboard(HttpServletRequest request, Model model, @CookieValue(value = "accessToken") String token) {
        String id = jwt.getLoginId(token);
        AdminDto dto = mergeServ.selectMyPage(id);
        if (dto == null) return "redirect:/adminLogin?error=true";
        model.addAttribute("admin", dto);

        return "admin/headDashboard";
    }
}