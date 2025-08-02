package com.hackathon.pr_review_assistant.controller;

import com.hackathon.pr_review_assistant.service.GitHubService;
import com.hackathon.pr_review_assistant.service.OpenAIService;
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

    @Autowired
    private OpenAIService openAIService;

    /*@GetMapping("/{owner}/{repo}/{prNumber}/review")
    public String getPRReview(
            @PathVariable String owner,
            @PathVariable String repo,
            @PathVariable int prNumber) {

        String diff = gitHubService.fetchPullRequestDiff(owner, repo, prNumber);
        return openAIService.generatePRReviewComment(diff);
    }*/

    @GetMapping("/{owner}/{repo}/{prNumber}/review")
    public String getPRReviewAndPostToGitHub(
            @PathVariable String owner,
            @PathVariable String repo,
            @PathVariable int prNumber) {

        String diff = gitHubService.fetchPullRequestDiff(owner, repo, prNumber);
        String review = openAIService.generatePRReviewComment(diff);
        String result = gitHubService.postCommentToPR(owner, repo, prNumber, review);
        return result + "\n\nReview:\n" + review;
    }
}
