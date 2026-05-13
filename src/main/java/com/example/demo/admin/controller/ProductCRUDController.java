package com.example.demo.admin.controller;

import com.example.demo.admin.dto.ProductDto;
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

    // 상품 등록 페이지
    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("product", new ProductDto());

        return "admin/product/registerProduct";
    }

    // 상품 등록
    @PostMapping("/register")
    public String registerProduct(ProductDto dto,
                                  HttpSession session) {

        // 등록자 세팅
        String loginId =
                (String) session.getAttribute("loginId");

        dto.setCreated_by(loginId);

        // 사용 여부 기본값
        dto.setIs_active("Y");

        serv.registerPro(dto);

        return "redirect:/admin/product/list";
    }


    // 상품 목록 조회
    @GetMapping("/list")
    public String listPro(Model model) {

        List<ProductDto> list =
                serv.listPro();

        model.addAttribute("list", list);

        return "admin/product/productList";
    }

    // 상태별 조회
    @GetMapping("/status")
    public String selectByStatus(
            @RequestParam("approve_status")
            String approve_status,
            Model model) {

        List<ProductDto> list =
                serv.selectByStatus(approve_status);

        model.addAttribute("list", list);

        return "admin/product/productList";
    }

    // 상품 상세 조회
    @GetMapping("/detail/{product_id}")
    public String listDetail(
            @PathVariable("product_id")
            Long product_id,
            Model model) {

        ProductDto dto =
                serv.listDetail(product_id);

        model.addAttribute("product", dto);

        return "admin/product/productDetail";
    }

    // 상품 수정 페이지
    @GetMapping("/update/{product_id}")
    public String updatePage(
            @PathVariable("product_id")
            Long product_id,
            Model model) {

        ProductDto dto =
                serv.listDetail(product_id);

        model.addAttribute("product", dto);

        return "admin/product/updateProduct";
    }

    // 상품 수정
    @PostMapping("/update")
    public String updateProduct(ProductDto dto,
                                HttpSession session) {

        String loginId =
                (String) session.getAttribute("loginId");

        dto.setUpdated_by(loginId);

        serv.updatePro(dto);

        return "redirect:/admin/product/detail/" +
                dto.getProduct_id();
    }

    // 상품 활성화 여부 변경
    @PostMapping("/status/update")
    public String updateStatus(ProductDto dto,
                               HttpSession session) {

        String loginId =
                (String) session.getAttribute("loginId");

        dto.setUpdated_by(loginId);

        serv.updateProductStatus(dto);

        return "redirect:/admin/product/list";
    }

    // 상품 삭제
    @GetMapping("/delete/{product_id}")
    public String deletePro(
            @PathVariable("product_id")
            Long product_id) {

        serv.deletePro(product_id);

        return "redirect:/admin/product/list";
    }

}
