package org.skypro.recommendService.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.recommendService.DTO.QueryObject;
import org.skypro.recommendService.DTO.RecommendationObject;
import org.skypro.recommendService.model.DynamicRecommendationRule;
import org.skypro.recommendService.repository.DynamicRecommendationRuleRepository;
import org.skypro.recommendService.repository.DynamicRuleStatRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static javax.management.Query.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DynamicRecommendationServiceTest {

    @Mock
    private DynamicRecommendationRuleRepository ruleRepository;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private DynamicRuleStatRepository dynamicRuleStatRepository;


    private DynamicRecommendationService service;

    @BeforeEach
    void setup() {
        service = new DynamicRecommendationService(
                Collections.emptyList(),
                ruleRepository,
                dynamicRuleStatRepository,
                jdbcTemplate,
                objectMapper
        );
    }

    @Test
    public void evaluateQuery_shouldHandleNegateCorrectly() throws Exception {
        QueryObject query = new QueryObject("USER_OF", List.of("DEBIT"), true);
        when(jdbcTemplate.queryForObject(anyString(), Boolean.class, any(), any()))
                .thenReturn(true);

        boolean result = service.evaluateQuery(query, UUID.randomUUID());

        assertFalse(result);
    }

    @Test
    public void getRecommendations_shouldAddDynamicRules() throws Exception {
        // Подготовка моков для динамического правила
        DynamicRecommendationRule rule = new DynamicRecommendationRule();
        rule.setProductName("Dynamic Product");
        when(ruleRepository.findAll()).thenReturn(List.of(rule));
        when(objectMapper.readValue(anyString(), any(TypeReference.class)))
                .thenReturn(List.of(new QueryObject("USER_OF", List.of("CREDIT"), false)));
        when(jdbcTemplate.queryForObject(anyString(), Boolean.class, any(), any()))
                .thenReturn(true);

        List<RecommendationObject> result = service.getListOfRecommendation(UUID.randomUUID());

        assertEquals(1, result.size());
        assertEquals("Dynamic Product", result.get(0).getName());
    }
}