package com.example.demo.fx_personal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FxController {

	private final FxService fxService;
	private final ObjectMapper mapper;

	@GetMapping("/fx_home")
	public String fx_home() {

		return "fx/fxHome";
	}

	@GetMapping("/fx_body")
	public String fx_body() {

		return "fx/fx_body";
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
}
