package org.skypro.recommendService.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.recommendService.DTO.RecommendationObject;
import org.skypro.recommendService.component.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private RecommendationRuleSet ruleSet1;
    @Mock
    private RecommendationRuleSet ruleSet2;

    private RecommendationService service;

    @BeforeEach
    void setup() {
        service = new RecommendationService(List.of(ruleSet1, ruleSet2));
    }

    @Test
    void getRecommendations_shouldCombineResultsFromAllRules() {
        UUID userId = UUID.randomUUID();
        RecommendationObject obj1 = new RecommendationObject(UUID.randomUUID(), "Product1", "Text1");
        RecommendationObject obj2 = new RecommendationObject(UUID.randomUUID(), "Product2", "Text2");

        when(ruleSet1.getRecommendation(userId)).thenReturn(Optional.of(List.of(obj1)));
        when(ruleSet2.getRecommendation(userId)).thenReturn(Optional.of(List.of(obj2)));

        List<RecommendationObject> result = service.getListOfRecommendation(userId);

        assertEquals(2, result.size());
        assertTrue(result.contains(obj1));
        assertTrue(result.contains(obj2));
    }
}
