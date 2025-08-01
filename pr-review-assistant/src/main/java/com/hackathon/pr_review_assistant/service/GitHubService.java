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
}
