package org.skypro.recommendService.service;

import org.skypro.recommendService.DTO.Recommendation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationService {
    private final List<RecommendationRuleSet> ruleSets;

    public RecommendationService(List<RecommendationRuleSet> ruleSets) {
        this.ruleSets = ruleSets;
    }

    public List<Recommendation> getListRecommendation(UUID userId) {
        List<Recommendation> allRecommendations = new ArrayList<>();
        for (RecommendationRuleSet ruleSet : ruleSets) {
            ruleSet.getRecommendation(userId).ifPresent(allRecommendations::addAll);
        }
        return allRecommendations;
    }


}
