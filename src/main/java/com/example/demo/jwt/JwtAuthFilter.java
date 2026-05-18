package com.example.demo.jwt;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	private JwtAuthFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		log.info("----------------| jwtFilter | ----------");
		String uri = request.getRequestURI();
		log.info("uri : {}", uri);

		if (uri.equals("/loginPage") || uri.equals("/loginProc") || uri.startsWith("/css/")
				|| uri.startsWith("/images/")) {
			filterChain.doFilter(request, response);
			return;
		}

		Cookie[] cookies = request.getCookies();
		String token = null;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("accessToken".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
		}

//
//		// 토큰 유효성 검사


		// 토큰 유효성 검사

//		if (!jwtUtil.isValid(token) || token == null) {
//			log.info("토큰이 없거나 만료되었습니다. 로그인 페이지로 이동합니다.");
//
//			Cookie deleteCookie = new Cookie("accessToken", null);
//			deleteCookie.setMaxAge(0);
//			deleteCookie.setPath("/");
//			response.addCookie(deleteCookie);
//
//			response.sendRedirect("/loginPage");
//			return; 
//		}

		filterChain.doFilter(request, response);
	}

}
