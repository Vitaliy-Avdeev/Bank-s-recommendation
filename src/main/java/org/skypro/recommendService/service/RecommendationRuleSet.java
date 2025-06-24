package org.skypro.recommendService.service;

import org.skypro.recommendService.DTO.Recommendation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<List<Recommendation>> getRecommendation(UUID userId);
}
