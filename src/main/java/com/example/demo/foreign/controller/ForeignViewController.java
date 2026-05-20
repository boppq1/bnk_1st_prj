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
	
	 // 인터넷외화송금서비스
	 @GetMapping("/remittance/remittanceInternet")
	 public String remittanceInternet() {
	     return "foreign/remittance/remittanceInternet";
	 }
	
	 // BNK공동해외송금서비스
	 @GetMapping("/remittance/remittanceBNKGlobal")
	 public String remittanceBNKGlobal() {
	     return "foreign/remittance/remittanceBNKGlobal";
	 }
	
	 // 해외즉시송금서비스
	 @GetMapping("/remittance/remittanceFast")
	 public String remittanceFast() {
	     return "foreign/remittance/remittanceFast";
	 }
	
	 // 해외즉시자동송금서비스
	 @GetMapping("/remittance/remittanceAuto")
	 public String remittanceAuto() {
	     return "foreign/remittance/remittanceAuto";
	 }
	
	 // 중국지역스피드송금서비스
	 @GetMapping("/remittance/remittanceChina")
	 public String remittanceChina() {
	     return "foreign/remittance/remittanceChina";
	 }
	
	 @GetMapping("/remittance/remittanceForeignAuto")
	 public String remittanceForeignAuto() {
	     return "foreign/remittance/remittanceForeignAuto";
	 }
	 
	 // 이종통화송금서비스
	 @GetMapping("/remittance/remittanceCurrency")
	 public String remittanceCurrency() {
	     return "foreign/remittance/remittanceCurrency";
	 }
	
	 // 지정환율자동송금서비스
	 @GetMapping("/remittance/remittanceRateAuto")
	 public String remittanceRateAuto() {
	     return "foreign/remittance/remittanceRateAuto";
	 }
	
	 // 모바일송금서비스
	 @GetMapping("/remittance/remittanceMobile")
	 public String remittanceMobile() {
	     return "foreign/remittance/remittanceMobile";
	 }
	
	 // 해외송금 E-mail 서비스
	 @GetMapping("/remittance/remittanceEmail")
	 public String remittanceEmail() {
	     return "foreign/remittance/remittanceEmail";
	 }
	
	 // 원화(KRW) 해외송금서비스
	 @GetMapping("/remittance/remittanceKRW")
	 public String remittanceKRW() {
	     return "foreign/remittance/remittanceKRW";
	 }
	
	 // =========================
	 // 외화송금보내기
	 // =========================
	
	 // 송금안내
	 @GetMapping("/remittance/remittanceGuide")
	 public String remittanceGuide() {
	     return "foreign/remittance/remittanceGuide";
	 }
	
	 // 송금방법
	 @GetMapping("/remittance/remittanceMethod")
	 public String remittanceMethod() {
	     return "foreign/remittance/remittanceMethod";
	 }
	
	 // 국가별송금시필요정보
	 @GetMapping("/remittance/remittanceCountryInfo")
	 public String remittanceCountryInfo() {
	     return "foreign/remittance/remittanceCountryInfo";
	 }
	
	 // =========================
	 // 외화송금받기
	 // =========================
	
	 @GetMapping("/remittance/remittanceReceive")
	 public String remittanceReceive() {
	     return "foreign/remittance/remittanceReceive";
	 }
	 
	// =========================
	// 수출업무
	// =========================

	@GetMapping("/tradeFinance/export/exportAgreement")
	public String exportAgreement() {
	    return "foreign/tradeFinance/export/exportAgreement";
	}

	@GetMapping("/tradeFinance/export/exportLCNotice")
	public String exportLCNotice() {
	    return "foreign/tradeFinance/export/exportLCNotice";
	}

	@GetMapping("/tradeFinance/export/exportLCTransfer")
	public String exportLCTransfer() {
	    return "foreign/tradeFinance/export/exportLCTransfer";
	}

	@GetMapping("/tradeFinance/export/exportBillPurchase")
	public String exportBillPurchase() {
	    return "foreign/tradeFinance/export/exportBillPurchase";
	}

	@GetMapping("/tradeFinance/export/exportCollection")
	public String exportCollection() {
	    return "foreign/tradeFinance/export/exportCollection";
	}

	@GetMapping("/tradeFinance/export/exportTracking")
	public String exportTracking() {
	    return "foreign/tradeFinance/export/exportTracking";
	}

	@GetMapping("/tradeFinance/export/exportDebtService")
	public String exportDebtService() {
	    return "foreign/tradeFinance/export/exportDebtService";
	}

	@GetMapping("/tradeFinance/export/exportForgaiting")
	public String exportForgaiting() {
	    return "foreign/tradeFinance/export/exportForgaiting";
	}


	// =========================
	// 수입업무
	// =========================

	@GetMapping("/tradeFinance/import/importAgreement")
	public String importAgreement() {
	    return "foreign/tradeFinance/import/importAgreement";
	}

	@GetMapping("/tradeFinance/import/importLCOpen")
	public String importLCOpen() {
	    return "foreign/tradeFinance/import/importLCOpen";
	}

	@GetMapping("/tradeFinance/import/importGuarantee")
	public String importGuarantee() {
	    return "foreign/tradeFinance/import/importGuarantee";
	}

	@GetMapping("/tradeFinance/import/importDocumentRelease")
	public String importDocumentRelease() {
	    return "foreign/tradeFinance/import/importDocumentRelease";
	}

	@GetMapping("/tradeFinance/import/importInternetLC")
	public String importInternetLC() {
	    return "foreign/tradeFinance/import/importInternetLC";
	}

	@GetMapping("/tradeFinance/import/importUsance")
	public String importUsance() {
	    return "foreign/tradeFinance/import/importUsance";
	}

	@GetMapping("/tradeFinance/import/importChinaTrade")
	public String importChinaTrade() {
	    return "foreign/tradeFinance/import/importChinaTrade";
	}


	// =========================
	// 내국신용장 / 무역금융
	// =========================

	@GetMapping("/tradeFinance/domestic/domesticLC")
	public String domesticLC() {
	    return "foreign/tradeFinance/domestic/domesticLC";
	}

	@GetMapping("/tradeFinance/domestic/domesticCollection")
	public String domesticCollection() {
	    return "foreign/tradeFinance/domestic/domesticCollection";
	}

	@GetMapping("/tradeFinance/domestic/domesticSettlement")
	public String domesticSettlement() {
	    return "foreign/tradeFinance/domestic/domesticSettlement";
	}

	@GetMapping("/tradeFinance/domestic/purchaseConfirm")
	public String purchaseConfirm() {
	    return "foreign/tradeFinance/domestic/purchaseConfirm";
	}

	@GetMapping("/tradeFinance/domestic/tradeFinanceGuide")
	public String tradeFinanceGuide() {
	    return "foreign/tradeFinance/domestic/tradeFinanceGuide";
	}


	// =========================
	// 전자무역
	// =========================

	@GetMapping("/tradeFinance/electronic/ediGuide")
	public String ediGuide() {
	    return "foreign/tradeFinance/electronic/ediGuide";
	}

	@GetMapping("/tradeFinance/electronic/ediApply")
	public String ediApply() {
	    return "foreign/tradeFinance/electronic/ediApply";
	}

	@GetMapping("/tradeFinance/electronic/ediSupport")
	public String ediSupport() {
	    return "foreign/tradeFinance/electronic/ediSupport";
	}


	// =========================
	// 수출입통관 상담
	// =========================

	@GetMapping("/tradeFinance/customsConsulting")
	public String customsConsulting() {
	    return "foreign/tradeFinance/customsConsulting";
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
	 // 국내외 투자
	 // templates/foreign/investment
	 // =========================
	
	 // 해외직접투자
	 @GetMapping("/investment/overseasInvestment")
	 public String overseasInvestment() {
	     return "foreign/investment/overseasInvestment";
	 }
	
	 // 해외사무소(지사)설치
	 @GetMapping("/investment/overseasBranch")
	 public String overseasBranch() {
	     return "foreign/investment/overseasBranch";
	 }
	
	 // 해외부동산투자
	 @GetMapping("/investment/overseasRealEstate")
	 public String overseasRealEstate() {
	     return "foreign/investment/overseasRealEstate";
	 }
	
	 // 외국인 국내 투자
	 @GetMapping("/investment/foreignDomesticInvestment")
	 public String foreignDomesticInvestment() {
	     return "foreign/investment/foreignDomesticInvestment";
	 }
	
	 // 외국인 국내 부동산 투자
	 @GetMapping("/investment/foreignDomesticRealEstate")
	 public String foreignDomesticRealEstate() {
	     return "foreign/investment/foreignDomesticRealEstate";
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
    
	 // =========================
	 // 글로벌파워셀러 특화서비스
	 // =========================
	
	 @GetMapping("/globalPowerSeller/globalPowerSellerGuide")
	 public String globalPowerSellerGuide() {
	     return "foreign/globalPowerSeller/globalPowerSellerGuide";
	 }
	 // =========================
	 // 외환이용가이드
	 // =========================
	
	 @GetMapping("/guide/foreignConsulting")
	 public String foreignConsulting() {
	     return "foreign/guide/foreignConsulting";
	 }
	
	 @GetMapping("/guide/foreignLaw")
	 public String foreignLaw() {
	     return "foreign/guide/foreignLaw";
	 }
	
	 @GetMapping("/guide/exportImportFee")
	 public String exportImportFee() {
	     return "foreign/guide/exportImportFee";
	 }
	
	 @GetMapping("/guide/exchangeRemittanceFee")
	 public String exchangeRemittanceFee() {
	     return "foreign/guide/exchangeRemittanceFee";
	 }
	
	 @GetMapping("/guide/foreignFAQ")
	 public String foreignFAQ() {
	     return "foreign/guide/foreignFAQ";
	 }
}