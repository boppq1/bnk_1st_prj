//package com.example.demo.admin.interceptor;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//
//@RequiredArgsConstructor
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws IOException, ServletException {
//
//        String token = resolve(request);
//
//        try {
//
//            if (token != null) {
//
//                // 예외 발생
//                if (!jwtUtil.validate(token)) {
//                    throw new RuntimeException("INVALID_TOKEN");
//                }
//
//                Long adminId = jwtUtil.getAdminId(token);
//
//                request.setAttribute("adminId", adminId);
//            }
//
//            filterChain.doFilter(request, response);
//
//        } catch (Exception e) {
//
//            // 401 반환되면 토큰 만료
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write("{\"message\":\"TOKEN_EXPIRED\"}");
//        }
//    }
//
//    private String resolve(HttpServletRequest request) {
//        String bearer = request.getHeader("Authorization");
//
//        if (bearer != null && bearer.startsWith("Bearer ")) {
//            return bearer.substring(7);
//        }
//        return null;
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//
//        String path = request.getRequestURI();
//
//        return path.startsWith("/admin/adminLogin")
//                || path.startsWith("/admin/refresh")
//                || path.startsWith("/admin/adminMain")   // ✅ 메인 페이지 제외
//                || path.startsWith("/css/")
//                || path.startsWith("/js/")
//                || path.startsWith("/images/");
//    }
//
//
//
//}