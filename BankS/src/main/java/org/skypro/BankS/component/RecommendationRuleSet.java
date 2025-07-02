package org.skypro.BankS.component;

import org.skypro.BankS.DTO.RecommendationObject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<List<RecommendationObject>> getRecommendation(UUID id);
}
