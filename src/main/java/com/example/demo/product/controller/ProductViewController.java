package com.example.demo.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.product.dto.request.ProductListRequestDto;
import com.example.demo.product.service.ProductPdfService;
import com.example.demo.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;
    private final ProductPdfService productPdfService;

    // 개인 상품몰
    @GetMapping("/product/personal")
    public String personalProductPage(ProductListRequestDto dto, Model model) {

        dto.setTargetLarge("PERSONAL");

        model.addAttribute("pageTitle", "개인 상품몰");
        model.addAttribute("customerType", "personal");
        model.addAttribute("products", productService.getProductList(dto));

        return "product/productList";
    }

    // 기업 상품몰
    @GetMapping("/product/company")
    public String companyProductPage(ProductListRequestDto dto, Model model) {

        dto.setTargetLarge("COMPANY");

        model.addAttribute("pageTitle", "기업 상품몰");
        model.addAttribute("customerType", "company");
        model.addAttribute("products", productService.getProductList(dto));

        return "product/productCompany";
    }

    // 상품 상세
    @GetMapping("/product/{productId}")
    public String productDetailPage(
            @PathVariable("productId") Long productId,
            Model model
    ) {

        model.addAttribute("product",
                productService.getProductDetail(productId));

        model.addAttribute("pdf",
                productPdfService.getPdfByProductId(productId));

        return "product/productDetail";
    }
    
    // 외환 상품몰
    @GetMapping("/product/foreign")
    public String foreignProductPage(ProductListRequestDto dto, Model model) {

        model.addAttribute("pageTitle", "외환 상품몰");
        model.addAttribute("customerType", "foreign");

        // 외환 메인일 때: 외화예금 + 외화적금 전체 조회
        if (dto.getProductType() == null || dto.getProductType().isBlank()) {
            dto.setForeignOnly("Y");
        }

        model.addAttribute("products", productService.getProductList(dto));

        return "product/productForeign";
    }
}