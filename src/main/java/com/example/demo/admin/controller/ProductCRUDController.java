package com.example.demo.admin.controller;

import com.example.demo.admin.dto.AdminDto;
import com.example.demo.admin.dto.ProductDto;
import com.example.demo.admin.service.AdminMergeService;
import com.example.demo.admin.service.AdminProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductCRUDController {

    private final AdminProductService serv;
    private final AdminMergeService adminServ;

    // 상품 등록 페이지
    @GetMapping("/admin/productRegisterPage")
    public String registerPage(Model model,
                               HttpSession session) {

        Long adminId = (Long) session.getAttribute("adminId");

        if(adminId == null) {
            return "redirect:/adminLogin";
        }

        AdminDto dto = new AdminDto();
        dto.setAdmin_id(adminId);


        AdminDto admin = adminServ.selectMyPage(dto);

        model.addAttribute("admin", admin);
        model.addAttribute("product", new ProductDto());

        return "admin/productRegisterPage";
    }

    // 상품 등록
    @PostMapping("/admin/productRegister")
    public String registerProduct(ProductDto dto,
                                  HttpSession session) {

        // 등록자 세팅
        Long adminId = (Long) session.getAttribute("adminId");
        String loginId = (String) session.getAttribute("loginId");
        String name = (String) session.getAttribute("name");


        dto.setCreated_by(name);
        dto.setRequester_id(name);

        if(name == null || name.equals("")) {
            return "redirect:/adminLogin";
        }

        serv.registerProduct(dto);
        return "redirect:/admin/productListPage";
    }

    // 상품 목록 조회
    @GetMapping("/admin/productListPage")
    public String listPro(HttpSession session, Model model, @RequestParam(value = "page", defaultValue = "1") int page) {

        // 페이지네이션

        int pageSize = 7;
        int offset = (page - 1) * pageSize;
        List<ProductDto> products = serv.getProductList(offset, pageSize);

        int totalCount = serv.getTotalCount();
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        AdminDto admin =
                (AdminDto) session.getAttribute("admin");

        if(admin == null) {
            return "redirect:/adminLogin";
        }

        model.addAttribute("admin", admin);
        model.addAttribute("products", products);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", page);

        return "admin/productListPage";
    }

    // 상태별 조회
    @GetMapping("/admin/proStatus")
    public String selectByStatus(@RequestParam("approve_status")
                                 String approve_status,
                                 HttpSession session,
                                 Model model) {

        Long adminId = (Long) session.getAttribute("adminId");

        if(adminId == null) {
            return "redirect:/adminLogin";
        }

        AdminDto dto = new AdminDto();
        dto.setAdmin_id(adminId);

        AdminDto admin = adminServ.selectMyPage(dto);

        List<ProductDto> products =
                serv.getProductByStatus(approve_status);

        model.addAttribute("admin", admin);
        model.addAttribute("products", products);
        model.addAttribute("totalCount", products.size());

        return "admin/productListPage";
    }

    // 상품 상세 조회 (AJAX)
    @ResponseBody
    @GetMapping("/admin/product/detail/{product_id}")
    public ProductDto productDetail(
            @PathVariable("product_id")
            Long product_id) {

        return serv.getProductDetail(product_id);
    }

    // 상품 상태 변경
    @PostMapping("/admin/status/update")
    public String updateStatus(ProductDto dto,
                               HttpSession session) {

        String loginId =
                (String) session.getAttribute("loginId");

        dto.setUpdated_by(loginId);

        serv.updateProductStatus(dto);

        return "redirect:/admin/productListPage";
    }

    // 상품 수정 페이지
    @GetMapping("/admin/updatePro/{product_id}")
    public String updatePage(
            @PathVariable("product_id")
            Long product_id,
            HttpSession session,
            Model model) {
        AdminDto admin =  (AdminDto) session.getAttribute("admin");
        String loginId =
                (String) session.getAttribute("loginId");

        if (loginId == null) {
            return "redirect:/adminLogin";
        }

        ProductDto proDto =
                serv.getProductDetail(product_id);
        model.addAttribute("admin", admin);
        model.addAttribute("product", proDto);

        return "admin/productUpdatePage";
    }

    // 상품 수정 저장/제출
    @PostMapping("/admin/updateProWrite")
    public String updateProduct(
            ProductDto dto,
            @RequestParam("action") String action,
            HttpSession session) {

        String loginId =
                (String) session.getAttribute("loginId");

        if (loginId == null) {
            return "redirect:/adminLogin";
        }

        dto.setUpdated_by(loginId);

        // 저장
        if ("save".equals(action)) {
            dto.setApprove_status("SAVE");
            serv.saveProduct(dto);

            // 제출
        } else if ("submit".equals(action)) {
            dto.setApprove_status("SUBMIT");
            serv.submitProduct(dto);
        }

        return "redirect:/admin/productListPage";
    }

    // 상품 삭제
    @GetMapping("/deletePro/{product_id}")
    public String deletePro(
            @PathVariable("product_id")
            Long product_id) {

        serv.deleteProduct(product_id);

        return "redirect:/admin/productListPage";
    }



}
