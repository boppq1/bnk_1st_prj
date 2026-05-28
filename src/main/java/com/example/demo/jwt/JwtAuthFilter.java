package com.example.demo.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;


	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		log.info("요청 URI: {}", uri);

		// 무한 루프 방지
		if (isPublicPath(uri)) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = resolveToken(request);
		boolean isTokenValid = (token != null && jwtUtil.isValid(token));

		if (uri.startsWith("/admin/")) {
			if (!isTokenValid) {
				log.info("관리자 인증 실패. 로그아웃 페이지로 이동.");
				clearCookie(response);
				response.sendRedirect("/admin/logout");
				return;
			}
			
			// 관리자 권한 검증
			String role = jwtUtil.getRole(token);
			System.out.println("관리자 role : "+ role);
			if ((uri.equals("/admin/adminMyPage") || uri.equals("/admin/newsPage") || uri.equals("/admin/makeNewsPage") ||
				 uri.equals("/admin/adminEventListPage") || uri.equals("/admin/adminEventPage") || uri.equals("/admin/exchangeListPage") ||
				 uri.equals("/admin/adminAccountPage")
				 ) && !"chief".equals(role)) {
				response.sendRedirect("/admin/access-denied?role");
				return;
			} else if ((uri.equals("/admin/executiveMyPage") || uri.equals("/admin/executiveDashboard") || uri.equals("/admin/executiveDashboard") || 
						uri.equals("/admin/approval") || uri.equals("/admin/logs") || uri.equals("/admin/blacklist")
					)&& !"executive".equals(role)) {
				System.out.println("ㅎㅎㅎ");
				response.sendRedirect("/admin/access-denied");
				System.out.println("ㅎㅎ");
				return;
			} else if ((uri.equals("/admin/headMyPage") || uri.equals("/admin/headDashboard") || uri.equals("/admin/memberList") ||
						uri.equals("/admin/adminList") || uri.equals("/admin/productRegisterPage") || uri.equals("/admin/productListPage") ||
						uri.equals("/admin/searchManagementPage")
					)&& !"head".equals(role)) {
				response.sendRedirect("/admin/access-denied");
				return;
			}
		}
		// 일반 사용자/기업 마이페이지 로직
		else if (uri.equals("/myPage")) {
			if (!isTokenValid) {
				log.info("일반/기업 비로그인 사용자. 로그인 페이지로 이동.");
				response.sendRedirect("/loginPage");
				return;
			}

			String role = jwtUtil.getRole(token);
			if ("user".equals(role)) {
				response.sendRedirect("/personal/myPage");
				return;
			} else if ("company".equals(role)) {
				response.sendRedirect("/company/companyMyPage");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	// 예외 경로 관리
	private boolean isPublicPath(String uri) {
		return uri.startsWith("/admin/logout") ||
				uri.startsWith("/admin/access-denied") ||
				uri.startsWith("/adminLogin") ||
				uri.startsWith("/admin/login") ||
				uri.startsWith("/loginPage") ||
				uri.startsWith("/css/") ||
				uri.startsWith("/images/") ||
				uri.startsWith("/admin/adminMain") ||
				uri.startsWith("/admin/adminJoin");
	}

	// 쿠키에서 토큰 추출
	private String resolveToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) return null;
		for (Cookie cookie : cookies) {
			if ("accessToken".equals(cookie.getName())) return cookie.getValue();
		}
		return null;
	}

	// 쿠키 삭제 도우미
	private void clearCookie(HttpServletResponse response) {
		Cookie deleteCookie = new Cookie("accessToken", null);
		deleteCookie.setMaxAge(0);
		deleteCookie.setPath("/");
		response.addCookie(deleteCookie);
	}
}