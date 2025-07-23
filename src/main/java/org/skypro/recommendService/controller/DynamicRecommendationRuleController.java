package org.skypro.recommendService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.skypro.recommendService.DTO.DynamicRecommendationRuleDto;
import org.skypro.recommendService.model.DynamicRecommendationRule;
import org.skypro.recommendService.model.DynamicRuleStat;
import org.skypro.recommendService.service.DynamicRecommendationService;
import org.skypro.recommendService.service.DynamicRuleStatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rule")
public class DynamicRecommendationRuleController {
    private final DynamicRecommendationService service;
    private final DynamicRuleStatService statService;

    public DynamicRecommendationRuleController(DynamicRecommendationService service, DynamicRuleStatService statService) {
        this.service = service;
        this.statService = statService;
    }

    @PostMapping("/new")
    public DynamicRecommendationRule createRule(@RequestBody DynamicRecommendationRuleDto dto) throws JsonProcessingException {
        return service.createRule(dto);
    }

    @GetMapping()
    public Map<String, List<DynamicRecommendationRuleDto>> getAllRules() throws JsonProcessingException {
        return service.getAllRules();
    }


    @DeleteMapping("/{productId}")
    public void deleteRule(@PathVariable String productId) {
        service.deleteRule(productId);
    }

    @GetMapping("/stat")
    public Map<String, List<DynamicRuleStat>> getRuleStat() {
        return statService.getRuleStat();
    }

}


