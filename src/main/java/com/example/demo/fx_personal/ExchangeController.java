package com.example.demo.fx_personal;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.jwt.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller("fxPersonalExchangeController")
public class ExchangeController {

    private final ExchangeService service;
    private final JwtUtil jwtUtil;

    public ExchangeController(ExchangeService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    /** 전체 통화 최신 환율 조회 (페이지 로딩 시 호출) */
    @GetMapping("/fx/rates")
    @ResponseBody
    public List<ExchangeRateDto> getRates() {
        return service.getLatestRates();
    }

    /** 환전 신청 처리 */
    @PostMapping("/fx/exchange")
    @ResponseBody
    public ExchangeResultDto doExchange(@RequestBody ExchangeRequestDto req,
                                        HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.isValid(token)) {
            return ExchangeResultDto.fail("로그인이 필요합니다.");
        }
        String loginId = jwtUtil.getLoginId(token);
        System.out.println("------"+loginId);
        Long usrNo = service.getUsrNo(loginId);
        if (usrNo == null) {
            return ExchangeResultDto.fail("사용자 정보를 찾을 수 없습니다.");
        }
        return service.exchange(usrNo, req);
    }

    private String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if ("accessToken".equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }
}