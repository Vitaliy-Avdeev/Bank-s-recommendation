package org.skypro.BankS.component;

import org.skypro.BankS.DTO.RecommendationObject;
import org.skypro.BankS.repository.RecommendationsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500Recommendation implements RecommendationRuleSet{
    private final RecommendationsRepository recommendationsRepository;

    public Invest500Recommendation(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public Optional<List<RecommendationObject>> getRecommendation(UUID id) {
        return recommendationsRepository.checkInvest500(id);
    }
}
