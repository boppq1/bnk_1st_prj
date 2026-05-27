package com.example.demo.fx_personal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FxController {

	private final FxService fxService;

	@GetMapping("/fx_home")
	public String fx_home() {

		return "fx/fxHome";
	}

	@GetMapping("/fx_body")
	public String fx_body() {

		return "fx/fx_body";
	}
	
	@GetMapping("/fx/exchange")
	public String exchange() {
		
		return "fx/exchange";
	}

	@ResponseBody
	@GetMapping("/api/exchange")
	public List<FxDto> getExchange(@RequestParam(name = "searchdate") String searchdate) {
		String json = fxService.getData(searchdate);
		ObjectMapper mapper = new ObjectMapper();
		try {
			JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, FxDto.class);
			return mapper.readValue(json, type);
		} catch (JsonProcessingException e) {
			return Collections.emptyList();
		}

	}
	
	@GetMapping("/view/rateTable")
	public String rateTablePage() {
		System.out.println(fxService.getFxData("미국 달러"));
	    return "fx/viewRate"; 
	}
	
	@ResponseBody
	@GetMapping("/viewRate")
	public List<FxDataDto> viewRate(@RequestParam(name = "cur_nm") String cur_nm){
		
		return (List<FxDataDto>) fxService.getFxData(cur_nm);
	}
	
	
	
}
