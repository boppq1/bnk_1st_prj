package com.example.demo.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AiQuestionRequest(@JsonProperty("question") String question) {}
