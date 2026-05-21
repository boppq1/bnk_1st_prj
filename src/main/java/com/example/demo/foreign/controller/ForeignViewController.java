package com.example.demo.foreign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.product.dto.request.ProductListRequestDto;
import com.example.demo.product.service.ProductService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/foreign")
public class ForeignViewController {

	private final ProductService productService;
	private final JwtUtil jwt;

	private void setLoginName(Model model, HttpServletRequest request) {
		String name = "";
		model.addAttribute("name", null);

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			System.out.println("쿠키체크");

			for (Cookie c : cookies) {
				if ("accessToken".equals(c.getName())) {
					String token = c.getValue();

					name = jwt.getUsername(token);

					System.out.println("name " + name);
					model.addAttribute("name", name);

					break;
				}
			}
		}

		System.out.println("마지막 name 값 " + name);
	}

	// =========================
	// 외화예금
	// =========================

	@GetMapping("/deposit/foreignDepositProduct")
	public String foreignDepositProduct(ProductListRequestDto dto, Model model, HttpServletRequest request) {
		setLoginName(model, request);

		dto.setProductType("FOREIGN_DEP");

		model.addAttribute("products", productService.getProductList(dto));

		return "foreign/deposit/foreignDepositProduct";
	}

	@GetMapping("/deposit/foreignDepositGuide")
	public String foreignDepositGuide(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/deposit/foreignDepositGuide";
	}

	@GetMapping("/deposit/foreignDepositBenefit")
	public String foreignDepositBenefit(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/deposit/foreignDepositBenefit";
	}

	@GetMapping("/deposit/foreignTransfer")
	public String foreignTransfer(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/deposit/foreignTransfer";
	}

	@GetMapping("/deposit/foreignAccountOpen")
	public String foreignAccountOpen(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/deposit/foreignAccountOpen";
	}

	// =========================
	// 외화송금
	// =========================

	@GetMapping("/remittance/remittanceInternet")
	public String remittanceInternet(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceInternet";
	}

	@GetMapping("/remittance/remittanceBNKGlobal")
	public String remittanceBNKGlobal(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceBNKGlobal";
	}

	@GetMapping("/remittance/remittanceFast")
	public String remittanceFast(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceFast";
	}

	@GetMapping("/remittance/remittanceAuto")
	public String remittanceAuto(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceAuto";
	}

	@GetMapping("/remittance/remittanceChina")
	public String remittanceChina(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceChina";
	}

	@GetMapping("/remittance/remittanceForeignAuto")
	public String remittanceForeignAuto(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceForeignAuto";
	}

	@GetMapping("/remittance/remittanceCurrency")
	public String remittanceCurrency(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceCurrency";
	}

	@GetMapping("/remittance/remittanceRateAuto")
	public String remittanceRateAuto(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceRateAuto";
	}

	@GetMapping("/remittance/remittanceMobile")
	public String remittanceMobile(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceMobile";
	}

	@GetMapping("/remittance/remittanceEmail")
	public String remittanceEmail(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceEmail";
	}

	@GetMapping("/remittance/remittanceKRW")
	public String remittanceKRW(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceKRW";
	}

	@GetMapping("/remittance/remittanceGuide")
	public String remittanceGuide(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceGuide";
	}

	@GetMapping("/remittance/remittanceMethod")
	public String remittanceMethod(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceMethod";
	}

	@GetMapping("/remittance/remittanceCountryInfo")
	public String remittanceCountryInfo(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceCountryInfo";
	}

	@GetMapping("/remittance/remittanceReceive")
	public String remittanceReceive(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/remittance/remittanceReceive";
	}

	// =========================
	// 수출업무
	// =========================

	@GetMapping("/tradeFinance/export/exportAgreement")
	public String exportAgreement(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/export/exportAgreement";
	}

	@GetMapping("/tradeFinance/export/exportLCNotice")
	public String exportLCNotice(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/export/exportLCNotice";
	}

	@GetMapping("/tradeFinance/export/exportLCTransfer")
	public String exportLCTransfer(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/export/exportLCTransfer";
	}

	@GetMapping("/tradeFinance/export/exportBillPurchase")
	public String exportBillPurchase(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/export/exportBillPurchase";
	}

	@GetMapping("/tradeFinance/export/exportCollection")
	public String exportCollection(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/export/exportCollection";
	}

	@GetMapping("/tradeFinance/export/exportTracking")
	public String exportTracking(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/export/exportTracking";
	}

	@GetMapping("/tradeFinance/export/exportDebtService")
	public String exportDebtService(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/export/exportDebtService";
	}

	@GetMapping("/tradeFinance/export/exportForgaiting")
	public String exportForgaiting(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/export/exportForgaiting";
	}

	// =========================
	// 수입업무
	// =========================

	@GetMapping("/tradeFinance/import/importAgreement")
	public String importAgreement(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/import/importAgreement";
	}

	@GetMapping("/tradeFinance/import/importLCOpen")
	public String importLCOpen(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/import/importLCOpen";
	}

	@GetMapping("/tradeFinance/import/importGuarantee")
	public String importGuarantee(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/import/importGuarantee";
	}

	@GetMapping("/tradeFinance/import/importDocumentRelease")
	public String importDocumentRelease(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/import/importDocumentRelease";
	}

	@GetMapping("/tradeFinance/import/importInternetLC")
	public String importInternetLC(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/import/importInternetLC";
	}

	@GetMapping("/tradeFinance/import/importUsance")
	public String importUsance(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/import/importUsance";
	}

	@GetMapping("/tradeFinance/import/importChinaTrade")
	public String importChinaTrade(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/import/importChinaTrade";
	}

	// =========================
	// 내국신용장 / 무역금융
	// =========================

	@GetMapping("/tradeFinance/domestic/domesticLC")
	public String domesticLC(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/domestic/domesticLC";
	}

	@GetMapping("/tradeFinance/domestic/domesticCollection")
	public String domesticCollection(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/domestic/domesticCollection";
	}

	@GetMapping("/tradeFinance/domestic/domesticSettlement")
	public String domesticSettlement(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/domestic/domesticSettlement";
	}

	@GetMapping("/tradeFinance/domestic/purchaseConfirm")
	public String purchaseConfirm(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/domestic/purchaseConfirm";
	}

	@GetMapping("/tradeFinance/domestic/tradeFinanceGuide")
	public String tradeFinanceGuide(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/domestic/tradeFinanceGuide";
	}

	// =========================
	// 전자무역
	// =========================

	@GetMapping("/tradeFinance/electronic/ediGuide")
	public String ediGuide(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/electronic/ediGuide";
	}

	@GetMapping("/tradeFinance/electronic/ediApply")
	public String ediApply(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/electronic/ediApply";
	}

	@GetMapping("/tradeFinance/electronic/ediSupport")
	public String ediSupport(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/electronic/ediSupport";
	}

	@GetMapping("/tradeFinance/customsConsulting")
	public String customsConsulting(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/tradeFinance/customsConsulting";
	}

	// =========================
	// 국내외 투자
	// =========================

	@GetMapping("/investment/overseasInvestment")
	public String overseasInvestment(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/investment/overseasInvestment";
	}

	@GetMapping("/investment/overseasBranch")
	public String overseasBranch(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/investment/overseasBranch";
	}

	@GetMapping("/investment/overseasRealEstate")
	public String overseasRealEstate(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/investment/overseasRealEstate";
	}

	@GetMapping("/investment/foreignDomesticInvestment")
	public String foreignDomesticInvestment(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/investment/foreignDomesticInvestment";
	}

	@GetMapping("/investment/foreignDomesticRealEstate")
	public String foreignDomesticRealEstate(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/investment/foreignDomesticRealEstate";
	}
	// =========================
	// 환전
	// =========================

	@GetMapping("/exchange/exchangeGuide")
	public String exchangeGuide(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/exchangeGuide";
	}

	@GetMapping("/exchange/exchangeCurrency")
	public String exchangeCurrency(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/exchangeCurrency";
	}

	@GetMapping("/exchange/exchangeAmount")
	public String exchangeAmount(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/exchangeAmount";
	}

	@GetMapping("/exchange/exchangeTip")
	public String exchangeTip(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/exchangeTip";
	}

	@GetMapping("/exchange/fakeMoneyGuide")
	public String fakeMoneyGuide(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/fakeMoneyGuide";
	}

	@GetMapping("/exchange/exchangeService")
	public String exchangeService(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/exchangeService";
	}

	@GetMapping("/exchange/autoExchange")
	public String autoExchange(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/autoExchange";
	}

	@GetMapping("/exchange/exchangeGift")
	public String exchangeGift(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/exchangeGift";
	}

	@GetMapping("/exchange/oneExchange")
	public String oneExchange(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/oneExchange";
	}

	@GetMapping("/exchange/exchangePreferential")
	public String exchangePreferential(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/exchangePreferential";
	}

	@GetMapping("/exchange/preferentialGuide")
	public String preferentialGuide(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/preferentialGuide";
	}

	@GetMapping("/exchange/preferentialType")
	public String preferentialType(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/preferentialType";
	}

	@GetMapping("/exchange/exchangeCalculator")
	public String exchangeCalculator(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/exchangeCalculator";
	}

	@GetMapping("/exchange/hubBranch")
	public String hubBranch(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/hubBranch";
	}

	@GetMapping("/exchange/foreignCheckGuide")
	public String foreignCheckGuide(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchange/foreignCheckGuide";
	}

	// =========================
	// 환율
	// =========================
	
	@GetMapping("/exchangeRate/rateNotification")
    public String rateNotification(Model model, HttpServletRequest request) {
        setLoginName(model, request);
        return "foreign/exchangeRate/rateNotification";
    }

	@GetMapping("/exchangeRate/emailRate")
	public String emailRate(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchangeRate/emailRate";
	}

	@GetMapping("/exchangeRate/freeRate")
	public String freeRate(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchangeRate/freeRate";
	}

	@GetMapping("/exchangeRate/riskManagement")
	public String riskManagement(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchangeRate/riskManagement";
	}

	@GetMapping("/exchangeRate/preferentialCoupon")
	public String preferentialCoupon(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/exchangeRate/preferentialCoupon";
	}

	// =========================
	// 유학 / 이주 / 여행
	// =========================

	@GetMapping("/studyMigrationTravel/studyGuide")
	public String studyGuide(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/studyMigrationTravel/studyGuide";
	}

	@GetMapping("/studyMigrationTravel/studyExchange")
	public String studyExchange(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/studyMigrationTravel/studyExchange";
	}

	@GetMapping("/studyMigrationTravel/studySupport")
	public String studySupport(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/studyMigrationTravel/studySupport";
	}

	@GetMapping("/studyMigrationTravel/migrationGuide")
	public String migrationGuide(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/studyMigrationTravel/migrationGuide";
	}

	@GetMapping("/studyMigrationTravel/migrationExchange")
	public String migrationExchange(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/studyMigrationTravel/migrationExchange";
	}

	@GetMapping("/studyMigrationTravel/travelNotice")
	public String travelNotice(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/studyMigrationTravel/travelNotice";
	}

	@GetMapping("/studyMigrationTravel/travelExchange")
	public String travelExchange(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/studyMigrationTravel/travelExchange";
	}

	// =========================
	// 부가서비스
	// =========================
	
	@GetMapping("/additionalService/foreignConsulting")
    public String add_foreignConsulting(Model model, HttpServletRequest request) {
        setLoginName(model, request);
        return "foreign/additionalService/foreignConsulting";
    }
	
	@GetMapping("/additionalService/foreignLaw")
    public String add_foreignLaw(Model model, HttpServletRequest request) {
        setLoginName(model, request);
        return "foreign/additionalService/foreignLaw";
    }
	
	@GetMapping("/additionalService/exportImportFee")
    public String add_exportImportFee(Model model, HttpServletRequest request) {
        setLoginName(model, request);
        return "foreign/additionalService/exportImportFee";
    }
	
	@GetMapping("/additionalService/exchangeRemittanceFee")
    public String add_exchangeRemittanceFee(Model model, HttpServletRequest request) {
        setLoginName(model, request);
        return "foreign/additionalService/exchangeRemittanceFee";
    }
	
	@GetMapping("/additionalService/foreignFAQ")
    public String add_foreignFAQ(Model model, HttpServletRequest request) {
        setLoginName(model, request);
        return "foreign/additionalService/foreignFAQ";
    }
	

	// =========================
	// 글로벌파워셀러 특화서비스
	// =========================

	@GetMapping("/globalPowerSeller/globalPowerSellerGuide")
	public String globalPowerSellerGuide(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/globalPowerSeller/globalPowerSellerGuide";
	}

	// =========================
	// 외환이용가이드
	// =========================

	@GetMapping("/guide/foreignConsulting")
	public String foreignConsulting(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/guide/foreignConsulting";
	}

	@GetMapping("/guide/foreignLaw")
	public String foreignLaw(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/guide/foreignLaw";
	}

	@GetMapping("/guide/exportImportFee")
	public String exportImportFee(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/guide/exportImportFee";
	}

	@GetMapping("/guide/exchangeRemittanceFee")
	public String exchangeRemittanceFee(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/guide/exchangeRemittanceFee";
	}

	@GetMapping("/guide/foreignFAQ")
	public String foreignFAQ(Model model, HttpServletRequest request) {
		setLoginName(model, request);
		return "foreign/guide/foreignFAQ";
	}

}