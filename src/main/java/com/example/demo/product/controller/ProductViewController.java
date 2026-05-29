package com.example.demo.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.product.dto.request.ProductListRequestDto;
import com.example.demo.product.service.ProductPdfService;
import com.example.demo.product.service.ProductService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;

@Controller
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;
    private final ProductPdfService productPdfService;
    private final JwtUtil jwt;
    // 개인 상품몰
    @GetMapping("/product/personal")
    public String personalProductPage(ProductListRequestDto dto, Model model, HttpServletRequest request) {
    	String name = "";
        dto.setTargetLarge("PERSONAL");
        Cookie[] cookies = request.getCookies();
        model.addAttribute("name",null);
        
        if(cookies != null) {
			System.out.println("쿠키체크");
        for(Cookie c : cookies) {
        	if("accessToken".equals(c.getName())) {
        		String token = c.getValue();
        		name = jwt.getUsername(token);
        		System.out.println("name " +name);
        		model.addAttribute("name", name);		
        		}
        	}
        }
        System.out.println("마지막 name 값 "+ name);
        model.addAttribute("pageTitle", "개인 상품몰");
        model.addAttribute("customerType", "personal");
        model.addAttribute("products", productService.getProductList(dto));
        return "product/productList";
    }

    // 기업 상품몰
    @GetMapping("/product/company")
    public String companyProductPage(ProductListRequestDto dto, Model model, HttpServletRequest request) {
    	String name = "";
        dto.setTargetLarge("COMPANY");
        Cookie[] cookies = request.getCookies();
        model.addAttribute("name",null);
        
        if(cookies != null) {
			System.out.println("쿠키체크");
        for(Cookie c : cookies) {
        	if("accessToken".equals(c.getName())) {
        		String token = c.getValue();
        		name = jwt.getUsername(token);
        		System.out.println("name " +name);
        		model.addAttribute("name", name);		
        		}
        	}
        }
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
    public String foreignProductPage(ProductListRequestDto dto, Model model, HttpServletRequest request) {
    	String name = "";
    	Cookie[] cookies = request.getCookies();
        model.addAttribute("name",null);
        if(cookies != null) {
			System.out.println("쿠키체크");
        for(Cookie c : cookies) {
        	if("accessToken".equals(c.getName())) {
        		String token = c.getValue();
        		name = jwt.getUsername(token);
        		System.out.println("name " +name);
        		model.addAttribute("name", name);		
        		}
        	}
        }
        model.addAttribute("pageTitle", "외환 상품몰");
        model.addAttribute("customerType", "foreign");

        // 외환 메인일 때: 외화예금 + 외화적금 전체 조회
        if (dto.getProductType() == null || dto.getProductType().isBlank()) {
            dto.setForeignOnly("Y");
        }

        model.addAttribute("products", productService.getProductList(dto));

        return "product/productForeign";
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        Path path = Paths.get(uploadDir).resolve(filename);
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\"")
                .body(resource);
    }


}