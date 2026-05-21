package com.example.demo.search;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchPageController {

	@GetMapping("/search_page")
	public String searchpage(String search_text){
		if(search_text.equals("외환")) {
			
			return "fx/fxHome";
		}
		return "403";
	}
}
