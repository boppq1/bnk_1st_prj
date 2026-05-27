package com.example.demo.fx_personal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final FxService fxService;

    @GetMapping("/rate")
    public ResponseEntity<ReturnCalculatorDto> getRate(
            @RequestParam(name = "curNm") String curNm,
            @RequestParam(name = "date") String date,
            @RequestParam(name = "buySell") String buySell,
            @RequestParam(name = "prefer", defaultValue = "0") int prefer
    ) {
        ReturnCalculatorDto dto = fxService.getRate(curNm, date, buySell, prefer);
        return ResponseEntity.ok(dto);
    }
}
