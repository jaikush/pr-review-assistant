package com.hackathon.pr_review_assistant.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubService {

    @Value("${github.token}")
    private String githubToken;

    public String fetchPullRequestDiff(String owner, String repo, int prNumber) {
        String url = String.format("https://api.github.com/repos/%s/%s/pulls/%d", owner, repo, prNumber);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        headers.set("Accept", "application/vnd.github.v3.diff");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        return response.getBody(); // this is the diff
    }

    public String postCommentToPR(String owner, String repo, int prNumber, String comment) {
        String url = String.format("https://api.github.com/repos/%s/%s/issues/%d/comments", owner, repo, prNumber);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = String.format("{\"body\": \"%s\"}", comment.replace("\"", "\\\"").replace("\n", "\\n"));
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return "Posted successfully";
        } catch (Exception e) {
            return "Error posting comment: " + e.getMessage();
        }
    }
}
