package com.example.demo.fx_personal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FxService {
	
	@Autowired
	FxDataDao fxDao;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String auth_key = 
    		"Eg9Yl9fEBQhpVXX1eyvo0QhAyHFM0Xiw";
    		//"VmNGokoNNC2jjX7I7WhAsGI5a5YUnnYk";

    public String getData(String searchdate) {

        String url = "https://oapi.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + auth_key + "&searchdate=" + searchdate + "&data=AP01";

        return restTemplate.getForObject(url, String.class);
    }
    
    public FxDataDto getFxData(String cur_nm) {
    	
    	FxDataDto fxData = fxDao.viewData(cur_nm);
    	
    	return fxData;
    }

}