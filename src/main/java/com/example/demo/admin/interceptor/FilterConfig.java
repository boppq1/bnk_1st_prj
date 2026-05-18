//package com.example.demo.admin.interceptor;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.example.demo.jwt.JwtUtil;
//
//import jakarta.servlet.Filter;
//
//
//@Configuration
//public class FilterConfig {
//	
//	@Bean
//	public FilterRegistrationBean<Filter> rateLimitFilterRegistration(RateLimitFilter rateLimitFilter) {
//		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
//		
//		bean.setFilter(rateLimitFilter);
//		bean.setOrder(2); // 해당 필터를 몇번째 숫자로 세워둘 것인가?
//		bean.addUrlPatterns("/*");
//		
//		return bean;
//	}
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
