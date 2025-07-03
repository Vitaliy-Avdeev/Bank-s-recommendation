package org.skypro.BankS.service;

import org.skypro.BankS.DTO.RecommendationObject;
import org.skypro.BankS.component.RecommendationRuleSet;
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

    /**
     * Внутренний метод, в котором мы получаем id пользователя и возвращаем список рекомендаций для этого пользователя
     * (в зависимости от того, подошел ли какой-то продукт пользователю).
     * Выполняется проверка по каждому продукту (всего в системе описано три продукта:
     * Invest500, SimpleCredit, TopSaving), в случае успешной проверки продукт попадает в рекомендации.
     *
     * @param userId - id клиента.
     * @return - возвращаем список рекомендаций.
     */

    public List<RecommendationObject> getListOfRecommendation(UUID userId) {
        List<RecommendationObject> allRecommendations = new ArrayList<>();
        for (RecommendationRuleSet ruleSet : recommendations) {
            ruleSet.getRecommendation(userId).ifPresent(allRecommendations::addAll);
        }
        return allRecommendations;
    }


}
