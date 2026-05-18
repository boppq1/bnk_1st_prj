package com.example.demo.foreign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.product.dto.request.ProductListRequestDto;
import com.example.demo.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/foreign")
public class ForeignViewController {

    private final ProductService productService;

    // =========================
    // 외화예금
    // =========================

    @GetMapping("/deposit/foreignDepositProduct")
    public String foreignDepositProduct(
            ProductListRequestDto dto,
            Model model
    ) {
        dto.setProductType("FOREIGN_DEP");

        model.addAttribute(
                "products",
                productService.getProductList(dto)
        );

        return "foreign/deposit/foreignDepositProduct";
    }

    @GetMapping("/deposit/foreignDepositGuide")
    public String foreignDepositGuide() {
        return "foreign/deposit/foreignDepositGuide";
    }

    @GetMapping("/deposit/foreignDepositBenefit")
    public String foreignDepositBenefit() {
        return "foreign/deposit/foreignDepositBenefit";
    }

    @GetMapping("/deposit/foreignTransfer")
    public String foreignTransfer() {
        return "foreign/deposit/foreignTransfer";
    }

    @GetMapping("/deposit/foreignAccountOpen")
    public String foreignAccountOpen() {
        return "foreign/deposit/foreignAccountOpen";
    }

    // =========================
    // 외화송금
    // =========================

    @GetMapping("/remittance/remittanceService")
    public String remittanceService() {
        return "foreign/remittance/remittanceService";
    }

    @GetMapping("/remittance/remittanceSend")
    public String remittanceSend() {
        return "foreign/remittance/remittanceSend";
    }

    @GetMapping("/remittance/remittanceReceive")
    public String remittanceReceive() {
        return "foreign/remittance/remittanceReceive";
    }

    // =========================
    // 환전
    // =========================

    @GetMapping("/exchange/exchangeGuide")
    public String exchangeGuide() {
        return "foreign/exchange/exchangeGuide";
    }

    @GetMapping("/exchange/exchangeCalculator")
    public String exchangeCalculator() {
        return "foreign/exchange/exchangeCalculator";
    }

    // =========================
    // 유학 / 이주 / 여행
    // =========================

    @GetMapping("/studyMigrationTravel/studyGuide")
    public String studyGuide() {
        return "foreign/studyMigrationTravel/studyGuide";
    }

    @GetMapping("/studyMigrationTravel/studyExchange")
    public String studyExchange() {
        return "foreign/studyMigrationTravel/studyExchange";
    }

    @GetMapping("/studyMigrationTravel/studySupport")
    public String studySupport() {
        return "foreign/studyMigrationTravel/studySupport";
    }

    @GetMapping("/studyMigrationTravel/migrationGuide")
    public String migrationGuide() {
        return "foreign/studyMigrationTravel/migrationGuide";
    }

    @GetMapping("/studyMigrationTravel/migrationExchange")
    public String migrationExchange() {
        return "foreign/studyMigrationTravel/migrationExchange";
    }

    @GetMapping("/studyMigrationTravel/travelNotice")
    public String travelNotice() {
        return "foreign/studyMigrationTravel/travelNotice";
    }

    @GetMapping("/studyMigrationTravel/travelExchange")
    public String travelExchange() {
        return "foreign/studyMigrationTravel/travelExchange";
    }
    
    // 국내외 투자 해외직접투자
    @GetMapping("/investment/overseasInvestment")
    public String 해외직접투자() {
    	return "foreign/investment/overseasInvestment";
    }
}