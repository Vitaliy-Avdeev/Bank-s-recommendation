package org.skypro.recommendService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.skypro.recommendService.DTO.DynamicRecommendationRuleDto;
import org.skypro.recommendService.DTO.QueryObject;
import org.skypro.recommendService.model.DynamicRecommendationRule;
import org.skypro.recommendService.model.DynamicRuleStat;
import org.skypro.recommendService.repository.DynamicRecommendationRuleRepository;
import org.skypro.recommendService.repository.DynamicRuleStatRepository;
import org.skypro.recommendService.service.DynamicRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
public class DynamicRecommendationRuleController {
    private final DynamicRuleStatRepository dynamicRuleStatRepository;
    private final DynamicRecommendationService service;

    public DynamicRecommendationRuleController(DynamicRuleStatRepository dynamicRuleStatRepository, DynamicRecommendationService service) {
        this.dynamicRuleStatRepository = dynamicRuleStatRepository;
        this.service = service;
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
    public ResponseEntity<?> deleteRule(@PathVariable String productId) {
        return ResponseEntity.ok(service.deleteRule(productId));
    }

    @GetMapping("/stat")
    public Map<String, List<DynamicRuleStat>> getRuleStat() {
        List<DynamicRuleStat> rules = dynamicRuleStatRepository.findAll();
        return Map.of("stats", rules);
    }

}


