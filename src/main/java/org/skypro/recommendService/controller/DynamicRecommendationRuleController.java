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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
public class DynamicRecommendationRuleController {

    private final DynamicRecommendationRuleRepository repository;
    private final ObjectMapper objectMapper;
    private final DynamicRuleStatRepository dynamicRuleStatRepository;

    public DynamicRecommendationRuleController(DynamicRecommendationRuleRepository repository, ObjectMapper objectMapper, DynamicRuleStatRepository dynamicRuleStatRepository) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.dynamicRuleStatRepository = dynamicRuleStatRepository;
    }

    @PostMapping("/new")
    public DynamicRecommendationRule createRule(@RequestBody DynamicRecommendationRuleDto dto) throws JsonProcessingException {
        DynamicRecommendationRule entity = new DynamicRecommendationRule();
        entity.setProductId(dto.getProductId());
        entity.setProductName(dto.getProductName());
        entity.setProductText(dto.getProductText());
        entity.setRule(objectMapper.writeValueAsString(dto.getRule()));
        DynamicRecommendationRule saved = repository.save(entity);

        dynamicRuleStatRepository.save(new DynamicRuleStat(entity.getId(), 0));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return saved;
    }

    @GetMapping()
    public Map<String, List<DynamicRecommendationRuleDto>> getAllRules() throws JsonProcessingException {
        List<DynamicRecommendationRule> rules = repository.findAll();
        List<DynamicRecommendationRuleDto> dtos = rules.stream().map(r -> {
            try {
                List<QueryObject> queries = objectMapper.readValue(r.getRule(), new TypeReference<List<QueryObject>>() {});
                DynamicRecommendationRuleDto dto = new DynamicRecommendationRuleDto();
                dto.setProductId(r.getProductId());
                dto.setProductText(r.getProductText());
                dto.setProductName(r.getProductName());
                dto.setRule(queries);

                int count = dynamicRuleStatRepository.countByRuleId(r.getId());
                dynamicRuleStatRepository.save(new DynamicRuleStat(r.getId(), count + 1));

                return dto;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();
        return Map.of("data", dtos);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteRule(@PathVariable String productId) {
        String id = repository.getIdByProductId(productId);
        dynamicRuleStatRepository.deleteByRuleId(id);
        repository.deleteByProductId(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stat")
    public Map<String, List<DynamicRuleStat>> getRuleStat() {
        List<DynamicRuleStat> rules = dynamicRuleStatRepository.findAll();
        return Map.of("stats", rules);
    }

}


