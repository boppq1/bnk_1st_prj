package com.example.demo.admin.service;

import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.demo.admin.dto.AiAnswerResponse;
import com.example.demo.admin.dto.AiQuestionRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiService {
	
	private final RestClient restClient;
	private final ObjectMapper om = new ObjectMapper();
	
	public AiService() {
		
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
	    requestFactory.setConnectTimeout(5000);
	    requestFactory.setReadTimeout(60000);
		
		this.restClient = RestClient.builder()
				.baseUrl("http://localhost:8000")
				.requestFactory(requestFactory)
				.build();
	}

	public String askToAi(String userQuestion) {

		AiQuestionRequest requestBody = new AiQuestionRequest(userQuestion);

		String response = restClient.post()
				.uri("/ai/ask")
				.contentType(MediaType.APPLICATION_JSON)
				.body(requestBody)
				.retrieve()
				.body(String.class);

		System.out.println(response);

		try {
			AiAnswerResponse aiResponse = om.readValue(response, AiAnswerResponse.class);
			System.out.println(aiResponse.answer());
			return aiResponse.answer();
		} catch (com.fasterxml.jackson.core.JsonProcessingException e) {
			throw new RuntimeException("AI 응답 JSON 파싱 실패: " + e.getMessage(), e);
		}
	}
}
