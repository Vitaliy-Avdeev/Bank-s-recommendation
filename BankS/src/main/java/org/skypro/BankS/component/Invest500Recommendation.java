package org.skypro.BankS.component;

import org.skypro.BankS.DTO.RecommendationObject;
import org.skypro.BankS.repository.RecommendationsRepository;
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
    /**
     * Метод проверяет подходит ли банковский продукт "Invest 500" заданному клиенту.
     *
     * @param id - ID клиента.
     * @return если проверка прошла успешно, продукт будет включен в список рекомендаций клиента.
     */

    @Override
    public Optional<List<RecommendationObject>> getRecommendation(UUID id) {
        return recommendationsRepository.checkInvest500(id);
    }
}
