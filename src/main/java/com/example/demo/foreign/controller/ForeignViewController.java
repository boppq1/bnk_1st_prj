package com.example.demo.foreign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foreign")
public class ForeignViewController {

    // =========================
    // 외화예금
    // =========================

    @GetMapping("/deposit/foreignDepositGuide")
    public String foreignDepositGuide() {
        return "foreign/deposit/foreignDepositGuide";
    }

    @GetMapping("/deposit/foreignDepositBenefit")
    public String foreignDepositBenefit() {
        return "foreign/deposit/foreignDepositBenefit";
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

}