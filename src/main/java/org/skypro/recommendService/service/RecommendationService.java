package org.skypro.recommendService.service;

import org.skypro.recommendService.DTO.RecommendationObject;
import org.skypro.recommendService.component.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationService {
    private final List<RecommendationRuleSet> recommendations;

    public RecommendationService(List<RecommendationRuleSet> recommendations) {
        this.recommendations = recommendations;
    }

    public List<RecommendationObject> getListOfRecommendation(UUID userId) {
        List<RecommendationObject> allRecommendations = new ArrayList<>();
        for (RecommendationRuleSet ruleSet : recommendations) {
            ruleSet.getRecommendation(userId).ifPresent(allRecommendations::addAll);
        }
        return allRecommendations;
    }


}
