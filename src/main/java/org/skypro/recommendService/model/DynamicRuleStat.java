package org.skypro.recommendService.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "dynamic_rule_stat")
public class DynamicRuleStat {
    @Id
    @Column(name = "rule_id")
    private String ruleId;
    @Column(name = "count")
    private int count;

    public DynamicRuleStat(String  ruleId, int count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public DynamicRuleStat() {}

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ruleId = " + ruleId +
                "\ncount=" + count;
    }
}
