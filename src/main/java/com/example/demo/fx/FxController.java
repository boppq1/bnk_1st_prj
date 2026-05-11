package com.example.demo.fx;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FxController {

	@GetMapping("/fx_home")
	public String fx_home(){
		
		return "fxHome";
	}
	
}
