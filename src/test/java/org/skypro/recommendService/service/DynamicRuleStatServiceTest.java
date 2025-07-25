package org.skypro.recommendService.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skypro.recommendService.model.DynamicRuleStat;
import org.skypro.recommendService.repository.DynamicRuleStatRepository;

import java.util.List;
import java.util.Map;

class DynamicRuleStatServiceTest {

    @Test
    void testGetRuleStat() {
        DynamicRuleStatRepository repository = Mockito.mock(DynamicRuleStatRepository.class);
        Mockito.when(repository.findAll()).thenReturn(List.of(new DynamicRuleStat("id", 2)));

        DynamicRuleStatService service = new DynamicRuleStatService(repository);
        Map<String, List<DynamicRuleStat>> result = service.getRuleStat();

        Assertions.assertTrue(result.containsKey("stats"));
        Assertions.assertEquals(1, result.get("stats").size());
    }
}
