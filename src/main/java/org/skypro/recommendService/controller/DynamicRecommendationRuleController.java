package org.skypro.recommendService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.skypro.recommendService.DTO.DynamicRecommendationRuleDto;
import org.skypro.recommendService.DTO.QueryObject;
import org.skypro.recommendService.model.DynamicRecommendationRule;
import org.skypro.recommendService.repository.DynamicRecommendationRuleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

    public DynamicRecommendationRuleController(DynamicRecommendationRuleRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createRule(@RequestBody DynamicRecommendationRuleDto dto) throws JsonProcessingException {
        DynamicRecommendationRule entity = new DynamicRecommendationRule();
        entity.setProductId(dto.getProductId());
        entity.setProductName(dto.getProductName());
        entity.setProductText(dto.getProductText());
        entity.setRule(objectMapper.writeValueAsString(dto.getRule()));
        DynamicRecommendationRule saved = repository.save(entity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping()
    public ResponseEntity<?> getAllRules() throws JsonProcessingException {
        List<DynamicRecommendationRule> rules = repository.findAll();
        List<DynamicRecommendationRuleDto> dtos = rules.stream().map(r -> {
            try {
                List<QueryObject> queries = objectMapper.readValue(r.getRule(), new TypeReference<List<QueryObject>>() {});
                DynamicRecommendationRuleDto dto = new DynamicRecommendationRuleDto();
                dto.setProductId(r.getProductId());
                dto.setProductText(r.getProductText());
                dto.setProductName(r.getProductName());
                dto.setRule(queries);
                return dto;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return ResponseEntity.ok(Map.of("data", dtos));
    }

    @DeleteMapping("/{productId}")
    @Transactional
    public ResponseEntity<?> deleteRule(@PathVariable String productId) {
        repository.deleteByProductId(productId);
        return ResponseEntity.ok().build();
    }
}


