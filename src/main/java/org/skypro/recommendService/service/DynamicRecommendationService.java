package org.skypro.recommendService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.skypro.recommendService.DTO.DynamicRecommendationRuleDto;
import org.skypro.recommendService.DTO.QueryObject;
import org.skypro.recommendService.DTO.RecommendationObject;
import org.skypro.recommendService.component.RecommendationRuleSet;
import org.skypro.recommendService.model.DynamicRecommendationRule;
import org.skypro.recommendService.model.DynamicRuleStat;
import org.skypro.recommendService.repository.DynamicRecommendationRuleRepository;
import org.skypro.recommendService.repository.DynamicRuleStatRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DynamicRecommendationService {
    private final List<RecommendationRuleSet> recommendations;

    private final DynamicRecommendationRuleRepository dynamicRuleRepository;
    private final DynamicRuleStatRepository dynamicRuleStatRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    private final Cache<String, Boolean> userOfCache = Caffeine.newBuilder().maximumSize(10000).build();
    private final Cache<String, Boolean> activeUserOfCache = Caffeine.newBuilder().maximumSize(10000).build();
    private final Cache<String, Boolean> transactionCompareCache = Caffeine.newBuilder().maximumSize(10000).build();

    public DynamicRecommendationService(List<RecommendationRuleSet> recommendations, DynamicRecommendationRuleRepository dynamicRuleRepository,
                                        DynamicRuleStatRepository dynamicRuleStatRepository, @Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.recommendations = recommendations;
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.dynamicRuleStatRepository = dynamicRuleStatRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public List<RecommendationObject> getListOfRecommendation(UUID userId) {
        List<RecommendationObject> allRecommendations = new ArrayList<>();

        for (RecommendationRuleSet ruleSet : recommendations) {
            ruleSet.getRecommendation(userId).ifPresent(allRecommendations::addAll);
        }

        List<DynamicRecommendationRule> dynamicRules = dynamicRuleRepository.findAll();
        for (DynamicRecommendationRule rule : dynamicRules) {
            try {
                List<QueryObject> queries = objectMapper.readValue(rule.getRule(), new TypeReference<>() {});
                boolean rulePassed = queries.stream().allMatch(q -> evaluateQuery(q, userId));
                if (rulePassed) {
                    allRecommendations.add(new RecommendationObject(
                            UUID.fromString(rule.getProductId()),
                            rule.getProductName(),
                            rule.getProductText()
                    ));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return allRecommendations;
    }

    boolean evaluateQuery(QueryObject query, UUID userId) {
        boolean result;
        switch (query.getQuery()) {
            case "USER_OF":
                result = checkUserOf(userId, query.getArguments().get(0));
                break;
            case "ACTIVE_USER_OF":
                result = checkActiveUserOf(userId, query.getArguments().get(0));
                break;
            case "TRANSACTION_SUM_COMPARE":
                result = checkTransactionSumCompare(userId, query.getArguments());
                break;
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                result = checkTransactionSumCompareDepositWithdraw(userId, query.getArguments());
                break;
            default:
                throw new IllegalArgumentException("Unknown query type: " + query.getQuery());
        }
        return query.isNegate() ? !result : result;
    }
    /**
     * Запрос, в котором мы проверям является ли пользователь USER_OF, для которого ведется поиск рекомендаций.
     * @param userId
     * @param productType
     * @return
     */
    private boolean checkUserOf(UUID userId, String productType) {
        String key = userId + ":" + productType;
        Boolean cached = userOfCache.getIfPresent(key);
        if (cached != null) return cached;

        Boolean result = jdbcTemplate.queryForObject(
                "SELECT count(*) > 0 FROM TRANSACTIONS t JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                        "WHERE t.USER_ID = ? AND p.TYPE = ?",
                Boolean.class,
                userId, productType);

        userOfCache.put(key, result);
        return Boolean.TRUE.equals(result);
    }
    /**
     * Запрос проверяет, является ли пользователь ACTIVE_USER_OF, для которого ведется поиск рекомендаций
     * @param userId
     * @param productType
     * @return
     */

    private boolean checkActiveUserOf(UUID userId, String productType) {
        String key = userId + ":" + productType;
        Boolean cached = activeUserOfCache.getIfPresent(key);
        if (cached != null) return cached;

        Boolean result = jdbcTemplate.queryForObject(
                "SELECT count(*) >= 5 FROM TRANSACTIONS t JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                        "WHERE t.USER_ID = ? AND p.TYPE = ?",
                Boolean.class,
                userId, productType);

        activeUserOfCache.put(key, result);
        return Boolean.TRUE.equals(result);
    }
    /**
     * Запрос сравнение суммы транзакций с константой — TRANSACTION_SUM_COMPARE
     * @param userId
     * @param args
     * @return
     */

    private boolean checkTransactionSumCompare(UUID userId, List<String> args) {
        String productType = args.get(0);
        String transactionType = args.get(1);
        String operator = args.get(2);
        int value = Integer.parseInt(args.get(3));

        String key = userId + ":" + String.join(":", args);
        Boolean cached = transactionCompareCache.getIfPresent(key);
        if (cached != null) return cached;

        String sql = "SELECT SUM(t.AMOUNT) FROM TRANSACTIONS t JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                "WHERE t.USER_ID = ? AND p.TYPE = ? AND t.TYPE = ?";

        Integer sum = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType, transactionType);
        sum = sum == null ? 0 : sum;

        boolean result = switch (operator) {
            case ">" -> sum > value;
            case "<" -> sum < value;
            case "=" -> sum == value;
            case ">=" -> sum >= value;
            case "<=" -> sum <= value;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };

        transactionCompareCache.put(key, result);
        return result;
    }
    /**
     * Запрос cравнение суммы пополнений с тратами по всем продуктам одного типа — TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW
     * @param userId
     * @param args
     * @return
     */

    private boolean checkTransactionSumCompareDepositWithdraw(UUID userId, List<String> args) {
        String productType = args.get(0);
        String operator = args.get(1);

        String key = userId + ":" + String.join(":", args);
        Boolean cached = transactionCompareCache.getIfPresent(key);
        if (cached != null) return cached;

        String sql = "SELECT SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN t.AMOUNT ELSE 0 END) AS total_deposit, " +
                "SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN t.AMOUNT ELSE 0 END) AS total_withdraw " +
                "FROM TRANSACTIONS t JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                "WHERE t.USER_ID = ? AND p.TYPE = ?";

        Map<String, Object> sums = jdbcTemplate.queryForMap(sql, userId, productType);
        int deposit = ((Number) sums.getOrDefault("total_deposit", 0)).intValue();
        int withdraw = ((Number) sums.getOrDefault("total_withdraw", 0)).intValue();

        boolean result = switch (operator) {
            case ">" -> deposit > withdraw;
            case "<" -> deposit < withdraw;
            case "=" -> deposit == withdraw;
            case ">=" -> deposit >= withdraw;
            case "<=" -> deposit <= withdraw;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };

        transactionCompareCache.put(key, result);
        return result;
    }

    public Map<String, List<DynamicRecommendationRuleDto>> getAllRules(){
        List<DynamicRecommendationRule> rules = dynamicRuleRepository.findAll();
        List<DynamicRecommendationRuleDto> dtos = new ArrayList<>();
        for (DynamicRecommendationRule r : rules) {
            List<QueryObject> queries = null;
            try {
                queries = objectMapper.readValue(r.getRule(), new TypeReference<List<QueryObject>>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            DynamicRecommendationRuleDto dto = new DynamicRecommendationRuleDto();
            dto.setProductId(r.getProductId());
            dto.setProductText(r.getProductText());
            dto.setProductName(r.getProductName());
            dto.setRule(queries);

            int count = dynamicRuleStatRepository.countByRuleId(r.getId());
            dynamicRuleStatRepository.save(new DynamicRuleStat(r.getId(), count + 1));

            dtos.add(dto);
        }
        return Map.of("data", dtos);
    }

    public DynamicRecommendationRule createRule(@RequestBody DynamicRecommendationRuleDto dto) throws JsonProcessingException {
        DynamicRecommendationRule entity = new DynamicRecommendationRule();
        entity.setProductId(dto.getProductId());
        entity.setProductName(dto.getProductName());
        entity.setProductText(dto.getProductText());
        entity.setRule(objectMapper.writeValueAsString(dto.getRule()));
        DynamicRecommendationRule saved = dynamicRuleRepository.save(entity);

        dynamicRuleStatRepository.save(new DynamicRuleStat(entity.getId(), 0));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return saved;
    }

    public ResponseEntity<?> deleteRule(@PathVariable String productId) {
        String id = dynamicRuleRepository.getIdByProductId(productId);
        dynamicRuleStatRepository.deleteByRuleId(id);
        dynamicRuleRepository.deleteByProductId(productId);
        return ResponseEntity.ok().build();
    }
}