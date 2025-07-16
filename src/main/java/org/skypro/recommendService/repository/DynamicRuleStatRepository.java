package org.skypro.recommendService.repository;

import jakarta.transaction.Transactional;
import org.skypro.recommendService.model.DynamicRuleStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicRuleStatRepository extends JpaRepository<DynamicRuleStat, String> {
    int countByRuleId(String ruleId);
    @Transactional
    void deleteByRuleId(String ruleId);
}
