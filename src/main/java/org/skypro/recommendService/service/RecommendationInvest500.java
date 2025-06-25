package org.skypro.recommendService.service;

import org.skypro.recommendService.DTO.Recommendation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.github.benmanes.caffeine.cache.Cache;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationInvest500 implements RecommendationRuleSet {
    private final JdbcTemplate jdbcTemplate;
    private final Cache<String, Boolean> cache;

    public RecommendationInvest500(JdbcTemplate jdbcTemplate, Cache<String, Boolean> cache) {
        this.jdbcTemplate = jdbcTemplate;
        this.cache = cache;
    }

    @Override
    public Optional<List<Recommendation>> getRecommendation(UUID userId) {
        String cacheKeyDebit = userId + "_INVEST_DEBIT_COUNT";
        String cacheKeyInvest = userId + "_INVEST_PRODUCT_COUNT";
        String cacheKeySaving = userId + "_SAVING_SUM_OVER_1000";

        Boolean usesDebit = cache.getIfPresent(cacheKeyDebit);
        if (usesDebit == null) {
            usesDebit = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) > 0 FROM TRANSACTIONS t " +
                            "JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                            "WHERE t.USER_ID = ? AND p.TYPE = 'DEBIT'",
                    Boolean.class,
                    userId
            );
            cache.put(cacheKeyDebit, usesDebit);
        }
        Boolean noInvestProducts = cache.getIfPresent(cacheKeyInvest);
        if (noInvestProducts == null) {
            noInvestProducts = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) = 0 FROM TRANSACTIONS t " +
                            "JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                            "WHERE t.USER_ID = ? AND p.TYPE = 'INVEST'",
                    Boolean.class,
                    userId
            );
            cache.put(cacheKeyInvest, noInvestProducts);
        }
        Boolean savingOver1000 = cache.getIfPresent(cacheKeySaving);
        if (savingOver1000 == null) {
            savingOver1000 = jdbcTemplate.queryForObject(
                    "SELECT COALESCE(SUM(t.AMOUNT), 0) > 1000 " +
                            "FROM TRANSACTIONS t " +
                            "JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                            "WHERE t.USER_ID = ? AND p.TYPE = 'SAVING' AND t.TYPE = 'DEPOSIT'",
                    Boolean.class,
                    userId
            );
            cache.put(cacheKeySaving, savingOver1000);
        }
        if (Boolean.TRUE.equals(usesDebit) || Boolean.TRUE.equals(noInvestProducts) || Boolean.TRUE.equals(savingOver1000)) {
            String DESCRIPTION = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом";
            return Optional.of(List.of(
                    new Recommendation("Invest 500", userId, DESCRIPTION)
            ));
        }
        String NO_RECOMMENDATION = "No recommendation";
        return Optional.of(List.of(
                new Recommendation("Invest 500", userId, NO_RECOMMENDATION)
        ));
    }
}



