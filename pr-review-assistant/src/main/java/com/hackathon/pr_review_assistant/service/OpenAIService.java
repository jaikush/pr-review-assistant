package com.hackathon.pr_review_assistant.service;


import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
public class OpenAIService {

    private final OpenAiService openAiService;

    public OpenAIService(@Value("${openai.api.key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey);
    }

    public String generatePRReviewComment(String diff) {
        String systemPrompt = "You are a helpful code reviewer. Review the following git diff and provide feedback on code quality, potential bugs, style, and improvements.";
        ChatMessage systemMessage = new ChatMessage("system", systemPrompt);
        ChatMessage userMessage = new ChatMessage("user", diff);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4o-mini")  // or "gpt-4", or "gpt-3.5-turbo"
                .messages(List.of(systemMessage, userMessage))
                .maxTokens(500)
                .build();

        return openAiService.createChatCompletion(request).getChoices().get(0).getMessage().getContent();
    }
    @Value("${openai.api.key}")
    private String openAiApiKey;
    public String generatePRReviewCommentAlt(String diff) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAiApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = """
                {
                  "model": "gpt-4o",
                  "messages": [
                    { "role": "system", "content": "You are a senior software engineer. Review the following code diff and give suggestions." },
                    { "role": "user", "content": "%s" }
                  ],
                  "max_tokens": 500
                }
                """.formatted(diff);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "OpenAI API error: " + e.getMessage();
        }
    }
}

