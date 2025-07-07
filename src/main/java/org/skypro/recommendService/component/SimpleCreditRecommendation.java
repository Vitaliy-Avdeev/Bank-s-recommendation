package org.skypro.recommendService.component;

import org.skypro.recommendService.DTO.RecommendationObject;
import org.skypro.recommendService.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRecommendation implements RecommendationRuleSet{
    private final RecommendationsRepository recommendationsRepository;

    public SimpleCreditRecommendation(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }
    /**
     * Метод проверяет подходит ли банковский продукт "Простой кредит" заданному клиенту.
     *
     * @param id - ID клиента.
     * @return если проверка прошла успешно, продукт будет включен в список рекомендаций клиента.
     */

    @Override
    public Optional<List<RecommendationObject>> getRecommendation(UUID id) {
        return recommendationsRepository.checkSimpleCredit(id);
    }
}
