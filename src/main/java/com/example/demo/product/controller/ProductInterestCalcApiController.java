package com.example.demo.product.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.product.dto.request.ForeignInterestCalcRequestDto;
import com.example.demo.product.dto.response.InterestCalcResponseDto;
import com.example.demo.product.service.ProductInterestCalcService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductInterestCalcApiController {

    private final ProductInterestCalcService productInterestCalcService;

    @PostMapping("/api/products/calc/foreign")
    public InterestCalcResponseDto calculateForeign(
            @RequestBody ForeignInterestCalcRequestDto dto
    ) {
        return productInterestCalcService.calculateForeign(dto);
    }
}