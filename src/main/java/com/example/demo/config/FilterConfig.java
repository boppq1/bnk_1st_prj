package com.example.demo.config;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.interceptor.GuestIdentifierFilter;
//import com.example.demo.interceptor.JwtFilter;
import com.example.demo.interceptor.RateLimitFilter;
//import com.example.demo.jwt.JwtUtil;

import jakarta.servlet.Filter;


@Configuration
public class FilterConfig {
	
	@Bean
	public FilterRegistrationBean<Filter> rateLimitFilterRegistration(RateLimitFilter rateLimitFilter) {
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
		
		bean.setFilter(rateLimitFilter);
		bean.setOrder(2);
		bean.addUrlPatterns("/*");
		
		return bean;
	}
	
	@Bean
	public FilterRegistrationBean<Filter> guestIdentifierFilterRegistration(GuestIdentifierFilter filter) {
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
		bean.setFilter(filter);
		bean.setOrder(1);
		bean.addUrlPatterns("/*");
		
		return bean;
	}

//    @Bean
//    public FilterRegistrationBean<JwtFilter> jwtFilter(JwtUtil jwtUtil) {
//
//        FilterRegistrationBean<JwtFilter> bean =
//                new FilterRegistrationBean<>();
//
//        bean.setFilter(new JwtFilter(jwtUtil)); // 🔥 직접 생성
//        bean.setOrder(3);
//
//        return bean;
//    }

}



