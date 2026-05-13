package com.example.demo.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.product.dto.request.ProductListRequestDto;
import com.example.demo.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;

    // 개인 상품몰
    @GetMapping("/product/personal")
    public String personalProductPage(ProductListRequestDto dto, Model model) {

        dto.setTargetCustomer("personal");

        model.addAttribute("pageTitle", "개인 상품몰");
        model.addAttribute("customerType", "personal");
        model.addAttribute("products", productService.getProductList(dto));

        return "product/productList";
    }


    // 기업 상품몰
    @GetMapping("/product/company")
    public String companyProductPage(ProductListRequestDto dto, Model model) {

        dto.setTargetCustomer("company");

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

        model.addAttribute(
                "product",
                productService.getProductDetail(productId)
        );

        return "product/productDetail";
    }
}