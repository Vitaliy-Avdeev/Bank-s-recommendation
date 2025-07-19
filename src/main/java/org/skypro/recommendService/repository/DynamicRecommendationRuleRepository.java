package org.skypro.recommendService.repository;

import jakarta.transaction.Transactional;
import org.skypro.recommendService.model.DynamicRecommendationRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DynamicRecommendationRuleRepository extends JpaRepository<DynamicRecommendationRule, String> {
    Optional<DynamicRecommendationRule> findByProductId(String product_id);
    @Transactional
    void deleteByProductId(String product_id);
    @Query(value = "select id from dynamic_recommendation_rules where product_id = :product_id limit 1", nativeQuery = true)
    String getIdByProductId(String product_id);
}

