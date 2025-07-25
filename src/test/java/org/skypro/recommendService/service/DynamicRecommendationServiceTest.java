package org.skypro.recommendService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skypro.recommendService.DTO.QueryObject;
import org.skypro.recommendService.repository.DynamicRecommendationRuleRepository;
import org.skypro.recommendService.repository.DynamicRuleStatRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class DynamicRecommendationServiceTest {

    private DynamicRecommendationService service;
    private JdbcTemplate jdbcTemplate;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        objectMapper = new ObjectMapper();
        service = new DynamicRecommendationService(
                List.of(),
                Mockito.mock(DynamicRecommendationRuleRepository.class),
                Mockito.mock(DynamicRuleStatRepository.class),
                jdbcTemplate,
                objectMapper
        );
    }

    @Test
    void testCheckUserOf() {
        UUID userId = UUID.randomUUID();
        Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(), eq(Boolean.class), any(), any()))
                .thenReturn(true);

        boolean result = service.evaluateQuery(new QueryObject("USER_OF", List.of("DEBIT"), false), userId);
        Assertions.assertTrue(result);
    }
}
