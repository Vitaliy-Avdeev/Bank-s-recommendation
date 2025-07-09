package org.skypro.recommendService.repository;

import org.skypro.recommendService.model.DynamicRecommendationRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DynamicRecommendationRuleRepository extends JpaRepository<DynamicRecommendationRule, String> {
    Optional<DynamicRecommendationRule> findByProductId(String product_id);
    void deleteByProductId(String product_id);

}

