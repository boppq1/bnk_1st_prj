package com.example.demo.interceptor;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GuestIdentifierFilter implements Filter {

	private static final String COOKIE_NAME = "guest_id";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		// Nginx 등 프록시 환경 고려한 실제 IP 추출
		String clientIp = httpRequest.getHeader("X-Forwarded-For");
		
		if(clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getRemoteAddr();
		} else {
			clientIp = clientIp.split(",")[0].trim();
		}
		
		// HttpServletRequest나 ServletRequest나 동일한 것 같은데 왜 형변환을 해서 사용하는가?
		String guestId = null;
		
		Cookie[] cookies = httpRequest.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(COOKIE_NAME.equals(cookie.getName())) {
					guestId = cookie.getValue();
					break;
				}
			}
		}
		
		if(guestId == null) {
			guestId = UUID.randomUUID().toString();
			Cookie newCookie = new Cookie(COOKIE_NAME, guestId);
			newCookie.setPath("/");
			newCookie.setHttpOnly(true); // js로 쿠키 탈취 막는 보안 설정
			newCookie.setMaxAge(60 * 60 * 24); // 쿠키 유효기간 하루
			httpResponse.addCookie(newCookie);
		}
		
		String finalClientKey = clientIp + "_" + guestId;
		System.out.println("클라이언트 키 : " + finalClientKey);
		httpRequest.setAttribute("clientKey", finalClientKey);
		chain.doFilter(request, response);
	}

}
