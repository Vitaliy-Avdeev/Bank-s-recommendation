package org.skypro.recommendService.controller;

import org.skypro.recommendService.DTO.Recommendation;
import org.skypro.recommendService.service.RecommendationRuleSet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    private final List<RecommendationRuleSet> service;

    public RecommendationController(List<RecommendationRuleSet> recommendationServices) {
        this.service = recommendationServices;
    }

    @GetMapping("/{userId}")
    public List<Recommendation> getRecommendation(@PathVariable UUID userId) {

        List<Recommendation> allRecommendations = new ArrayList<>();
        for (RecommendationRuleSet service : service) {
            service.getRecommendation(userId).ifPresent(allRecommendations::addAll);
        }

        return allRecommendations;
    }
}
