package org.skypro.recommendService.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.recommendService.DTO.RecommendationObject;
import org.skypro.recommendService.repository.RecommendationsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationRulesTest {

    @Mock
    private RecommendationsRepository repository;

    private Invest500Recommendation invest500Rule;
    private TopSavingRecommendation topSavingRule;
    private SimpleCreditRecommendation simpleCreditRule;

    @BeforeEach
    void setup() {
        invest500Rule = new Invest500Recommendation(repository);
        topSavingRule = new TopSavingRecommendation(repository);
        simpleCreditRule = new SimpleCreditRecommendation(repository);
    }

    @Test
    void invest500_shouldReturnRecommendationWhenConditionsMet() {
        UUID userId = UUID.randomUUID();
        when(repository.checkInvest500(userId))
                .thenReturn(Optional.of(List.of(new RecommendationObject(UUID.randomUUID(), "Invest 500", "text"))));

        Optional<List<RecommendationObject>> result = invest500Rule.getRecommendation(userId);

        assertTrue(result.isPresent());
        assertEquals("Invest 500", result.get().get(0).getName());
    }

    @Test
    void topSaving_shouldReturnEmptyWhenConditionsNotMet() {
        UUID userId = UUID.randomUUID();
        when(repository.checkTopSaving(userId)).thenReturn(Optional.empty());

        Optional<List<RecommendationObject>> result = topSavingRule.getRecommendation(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    void simpleCredit_shouldHandleRepositoryException() {
        UUID userId = UUID.randomUUID();
        when(repository.checkSimpleCredit(userId)).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> simpleCreditRule.getRecommendation(userId));
    }
}
