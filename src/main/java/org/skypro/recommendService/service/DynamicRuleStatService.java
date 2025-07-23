package org.skypro.recommendService.service;

import org.skypro.recommendService.model.DynamicRuleStat;
import org.skypro.recommendService.repository.DynamicRuleStatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DynamicRuleStatService {
    private final DynamicRuleStatRepository repository;

    public DynamicRuleStatService(DynamicRuleStatRepository repository) {
        this.repository = repository;
    }

    public Map<String, List<DynamicRuleStat>> getRuleStat() {
        List<DynamicRuleStat> rules = repository.findAll();
        return Map.of("stats", rules);
    }
}
