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
        String name = session.getAttribute("name").toString();


        dto.setCreated_by(name);
        dto.setRequester_id(name);



        serv.registerProduct(dto);
        return "redirect:/admin/productListPage";
    }




    // 상품 목록 조회
//    @GetMapping("/admin/productListPage")
//    public String listPro(Model model) {
//
//        List<ProductDto> list = serv.listPro();
//        model.addAttribute("list", list);
//        return "admin/productListPage";
//    }

    // 상태별 조회
//    @GetMapping("/admin/proStatus")
//    public String selectByStatus(
//            @RequestParam("approve_status")
//            String approve_status,
//            Model model) {
//
//        List<ProductDto> list = serv.selectByStatus(approve_status);
//
//        model.addAttribute("list", list);
//
//        return "admin/product/productList";
//    }

    // 상품 상세 조회
//    @GetMapping("/detail/{product_no}")
//    public String listDetail(
//            @PathVariable("product_no")
//            Long product_no,
//            Model model) {
//
//        ProductDto dto = serv.(product_no);
//        model.addAttribute("product", dto);
//        return "admin/product/productDetail";
//    }

    // 상품 수정 페이지
//    @GetMapping("/update/{product_id}")
//    public String updatePage(
//            @PathVariable("product_id")
//            Long product_id,
//            Model model) {
//
//        ProductDto dto = serv.listDetail(product_id);
//
//        model.addAttribute("product", dto);
//
//        return "admin/product/updateProduct";
//    }

    // 상품 수정
//    @PostMapping("/update")
//    public String updateProduct(ProductDto dto,
//                                HttpSession session) {
//
//        String loginId =
//                (String) session.getAttribute("loginId");
//
//        dto.setUpdated_by(loginId);
//
//        serv.updateProduct(dto);
//
//        return "redirect:/admin/product/detail/" +
//                dto.getProduct_no();
//    }

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
//    @GetMapping("/delete/{product_id}")
//    public String deletePro(
//            @PathVariable("product_id")
//            Long product_id) {
//
//        serv.deletePro(product_id);
//
//        return "redirect:/admin/product/list";
//    }



}
