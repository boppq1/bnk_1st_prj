package com.example.demo.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.admin.dto.AiAnswerResponse;
import com.example.demo.admin.dto.AiQuestionRequest;
import com.example.demo.admin.service.AiService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
	private final AiService service;
	
	public ChatController(AiService service) {
		this.service = service;
	}
	
	@PostMapping("/ask")
	public ResponseEntity<AiAnswerResponse> askQuestion(@RequestBody AiQuestionRequest userRequest) {
		String question = userRequest.question();
		String answer = service.askToAi(question);
		System.out.println(answer);
		AiAnswerResponse response = new AiAnswerResponse(answer);
		return ResponseEntity.ok(response);
	}
	
}
