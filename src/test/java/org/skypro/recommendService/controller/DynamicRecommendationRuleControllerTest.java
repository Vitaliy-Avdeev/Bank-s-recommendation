package org.skypro.recommendService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skypro.recommendService.DTO.DynamicRecommendationRuleDto;
import org.skypro.recommendService.model.DynamicRecommendationRule;
import org.skypro.recommendService.model.DynamicRuleStat;
import org.skypro.recommendService.service.DynamicRecommendationService;
import org.skypro.recommendService.service.DynamicRuleStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DynamicRecommendationRuleController.class)
class DynamicRecommendationRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DynamicRecommendationService service;

    @MockitoBean
    private DynamicRuleStatService statService;

    @Test
    void testCreateRule() throws Exception {
        DynamicRecommendationRuleDto dto = new DynamicRecommendationRuleDto("name", "id", "text", List.of());
        DynamicRecommendationRule rule = new DynamicRecommendationRule("name", "id", "text", "[]");

        Mockito.when(service.createRule(Mockito.any())).thenReturn(rule);

        mockMvc.perform(post("/rule/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("name"));
    }

    @Test
    void testGetAllRules() throws Exception {
        Mockito.when(service.getAllRules()).thenReturn(Map.of("data", List.of()));

        mockMvc.perform(get("/rule"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteRule() throws Exception {
        mockMvc.perform(delete("/rule/test-product-id"))
                .andExpect(status().isOk());

        Mockito.verify(service).deleteRule("test-product-id");
    }

    @Test
    void testGetRuleStat() throws Exception {
        Map<String, List<DynamicRuleStat>> stats = Map.of("stats", List.of(new DynamicRuleStat("id", 1)));
        Mockito.when(statService.getRuleStat()).thenReturn(stats);

        mockMvc.perform(get("/rule/stat"))
                .andExpect(status().isOk());
    }
}
