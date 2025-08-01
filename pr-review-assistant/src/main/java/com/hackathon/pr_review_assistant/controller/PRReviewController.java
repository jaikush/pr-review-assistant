package com.hackathon.pr_review_assistant.controller;

import com.hackathon.pr_review_assistant.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pr-review")
public class PRReviewController {

    @Autowired
    private GitHubService gitHubService;

    @GetMapping("/{owner}/{repo}/{prNumber}")
    public String getPRDiff(
            @PathVariable String owner,
            @PathVariable String repo,
            @PathVariable int prNumber) {

        return gitHubService.fetchPullRequestDiff(owner, repo, prNumber);
    }
}
