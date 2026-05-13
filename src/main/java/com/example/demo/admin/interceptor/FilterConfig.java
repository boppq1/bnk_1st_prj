//package com.example.demo.admin.interceptor;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class FilterConfig {
//
//    @Bean
//    public FilterRegistrationBean<JwtFilter> jwtFilter(JwtUtil jwtUtil) {
//
//        FilterRegistrationBean<JwtFilter> bean =
//                new FilterRegistrationBean<>();
//
//        bean.setFilter(new JwtFilter(jwtUtil)); // 🔥 직접 생성
//        bean.setOrder(1);
//
//        return bean;
//    }
//
//}
