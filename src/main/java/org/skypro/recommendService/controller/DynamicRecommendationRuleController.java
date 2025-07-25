package org.skypro.recommendService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.skypro.recommendService.DTO.DynamicRecommendationRuleDto;
import org.skypro.recommendService.model.DynamicRecommendationRule;
import org.skypro.recommendService.model.DynamicRuleStat;
import org.skypro.recommendService.service.DynamicRecommendationService;
import org.skypro.recommendService.service.DynamicRuleStatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    @ResponseStatus(HttpStatus.CREATED)
    public DynamicRecommendationRule createRule(@RequestBody DynamicRecommendationRuleDto dto){
        return service.createRule(dto);
    }

    @GetMapping()
    public Map<String, List<DynamicRecommendationRuleDto>> getAllRules(){
        return service.getAllRules();
    }


    @DeleteMapping("delete/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRule(@PathVariable String productId) {
        service.deleteRule(productId);
    }

    @GetMapping("/stat")
    public Map<String, List<DynamicRuleStat>> getRuleStat() {
        return statService.getRuleStat();
    }

}


