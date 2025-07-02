package org.skypro.BankS.controller;

import org.skypro.BankS.DTO.RecommendationObject;
import org.skypro.BankS.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public Map<UUID, List<RecommendationObject>> getListOfRecommendation(@PathVariable UUID userId) {
        Map<UUID, List<RecommendationObject>> map = new HashMap<>();
        if (userId != null) {
            map.put(userId, recommendationService.getListOfRecommendation(userId));
        }
        return map;
    }
}
