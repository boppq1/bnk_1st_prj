package com.example.demo.fx_personal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FxScheduler {
	
	private final RestTemplate restTemplate = new RestTemplate();
	 
	private final String auth_key = 
			"Eg9Yl9fEBQhpVXX1eyvo0QhAyHFM0Xiw";
	    	//"VmNGokoNNC2jjX7I7WhAsGI5a5YUnnYk";
	
	@Autowired
	private FxDataDao fxDataDao;
	
	@Scheduled(cron = "0 0 12 * * *") //12시 정각을 뜻함
	public void insertRateEveryHour() {
		
		String searchdate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		String url = "https://oapi.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + auth_key + "&searchdate=" + searchdate + "&data=AP01";
		String response = restTemplate.getForObject(url, String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
		    List<Map<String, Object>> list = mapper.readValue(response, List.class);

		    for (Map<String, Object> item : list) {
		        String ttbStr = item.get("ttb").toString().replace(",", "");
		        Double ttb = Double.parseDouble(ttbStr);

		        if (ttb == 0) continue;

		        String cur_nm     = (String) item.get("cur_nm");
		        Double tts        = Double.parseDouble(item.get("tts").toString().replace(",", ""));
		        Double deal_bas_r = Double.parseDouble(item.get("deal_bas_r").toString().replace(",", ""));

		        fxDataDao.insertRate(cur_nm, ttb, tts, deal_bas_r);
		    }
		} catch (Exception e) {
		    e.printStackTrace(); // 오류 나면 콘솔에 출력
		}
		
	}
}
