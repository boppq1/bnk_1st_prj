package com.example.demo.search;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SearchPageController {

	final JwtUtil jwt;
	final SearchService ss;
	
	@GetMapping("/search_page")
	public String searchpage(@RequestParam("search_text") String search_text, @CookieValue(value="accessToken", required = false) String info){
		
		// 지우면 안됩니당
		String role_type = null;
		
		if(info == null) {
			System.out.println("널이다");
			role_type = "ROLE_GUEST";
		} else {
			role_type = jwt.getRole(info);
			if(role_type.equals("user")) {
				role_type = "ROLE_PERSONAL";
			} else {
				role_type = "ROLE_COMPANY";
			}
		}
		 
		ss.insertSearchKeyword(role_type, search_text);
		
		// 여기까지
		
		
		if(search_text.equals("외환")) {
			
			return "fx/fxHome";
		}
		return "403";
	}
}
