package com.example.demo.interceptor;

import com.example.demo.jwt.JwtUtil;
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
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private JwtFilter(JwtUtil jwtUtil) {this.jwtUtil = jwtUtil;}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String uri = request.getRequestURI();
        Cookie[] cookies = request.getCookies();
        String token = null;

        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if(!jwtUtil.isValid(token) && uri.equals("/admin/adminMyPage")){
            response.sendRedirect("/adminLogin");
            return;
        }

        if(jwtUtil.isValid(token) && uri.equals("/admin/adminMyPage")){
            if(jwtUtil.getRole(token).equals("chief")){
                response.sendRedirect("/admin/adminPage");
            }else if(jwtUtil.getRole(token).equals("executive")){
                response.sendRedirect("/admin/executiveMyPage");
            }else if(jwtUtil.getRole(token).equals("head")){
                response.sendRedirect("/admin/headMyPage");
            }
        }

        filterChain.doFilter(request, response);
    }


    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName())) return cookie.getValue();
        }
        return null;
    }


}