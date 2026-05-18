package com.example.demo.fx_personal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FxController {

	@GetMapping("/fx_home")
	public String fx_home(){
		
		return "fx/fxHome";
	}
	
	@GetMapping("/fx_body")
	public String fx_body() {
		
		return "fx/fx_body";
	}
}
