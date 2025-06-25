package org.skypro.recommendService.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.skypro.recommendService.DTO.Recommendation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationTopSaving implements RecommendationRuleSet {

    private final JdbcTemplate jdbcTemplate;
    private final Cache<String, Boolean> cache;

    public RecommendationTopSaving(JdbcTemplate jdbcTemplate, Cache<String, Boolean> cache) {
        this.jdbcTemplate = jdbcTemplate;
        this.cache = cache;
    }

    @Override
    public Optional<List<Recommendation>> getRecommendation(UUID userId) {
        String cacheKeyHasDebit = userId + "_HAS_DEBIT_TRANSACTIONS";
        String cacheKeyIncomeOver50k = userId + "_INCOME_OVER_50K_DEBIT_OR_SAVING";
        String cacheKeyIncomeMoreThanSpending = userId + "_DEBIT_INCOME_MORE_THAN_SPENDING";

        Boolean hasDebit = cache.getIfPresent(cacheKeyHasDebit);
        if (hasDebit == null) {
            hasDebit = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) > 0 " +
                            "FROM PRODUCTS p " +
                            "JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID " +
                            "WHERE p.TYPE = 'DEBIT' AND t.USER_ID = ?",
                    Boolean.class,
                    userId
            ));
            cache.put(cacheKeyHasDebit, hasDebit);
        }
        Boolean incomeOver50k = cache.getIfPresent(cacheKeyIncomeOver50k);
        if (incomeOver50k == null) {
            Map<String, Object> result = jdbcTemplate.queryForMap(
                    "SELECT " +
                            "(SUM(CASE WHEN p.TYPE = 'DEBIT' THEN t.AMOUNT ELSE 0 END) >= 50000) AS debit_condition, " +
                            "(SUM(CASE WHEN p.TYPE = 'SAVING' THEN t.AMOUNT ELSE 0 END) >= 50000) AS saving_condition " +
                            "FROM PRODUCTS p " +
                            "JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID " +
                            "WHERE t.AMOUNT > 0 AND t.USER_ID = ?",
                    userId
            );
            Boolean debitCondition = (Boolean) result.get("debit_condition");
            Boolean savingCondition = (Boolean) result.get("saving_condition");

            incomeOver50k = (debitCondition != null && debitCondition) ||
                    (savingCondition != null && savingCondition);
            cache.put(cacheKeyIncomeOver50k, incomeOver50k);
        }
        Boolean incomeMoreThanSpending = cache.getIfPresent(cacheKeyIncomeOver50k);

        if (incomeMoreThanSpending == null) {
            incomeMoreThanSpending = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT SUM(CASE WHEN t.AMOUNT > 0 THEN t.AMOUNT ELSE 0 END) > " +
                            "SUM(CASE WHEN t.AMOUNT < 0 THEN ABS(t.AMOUNT) ELSE 0 END) " +
                            "FROM PRODUCTS p " +
                            "JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID " +
                            "WHERE p.TYPE = 'DEBIT' AND t.USER_ID = ?",
                    Boolean.class,
                    userId
            ));
            cache.put(cacheKeyIncomeOver50k, incomeMoreThanSpending);
        }
        if (hasDebit || incomeOver50k || incomeMoreThanSpending) {

            String DESCRIPTION = "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели";
            return Optional.of(List.of(new Recommendation("Top saving", userId, DESCRIPTION)));
        } else {
            String NO_RECOMMENDATION = "No recommendation";
            return Optional.of(List.of(new Recommendation("Top saving", userId, NO_RECOMMENDATION)));
        }
    }

}
