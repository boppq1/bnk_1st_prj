package com.example.demo.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AiAnswerResponse(@JsonProperty("answer") String answer) {}
