package org.skypro.recommendService.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skypro.recommendService.DTO.RecommendationObject;
import org.skypro.recommendService.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendationController.class)
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecommendationService recommendationService;

    @Test
    void testGetListOfRecommendation() throws Exception {
        UUID userId = UUID.randomUUID();
        List<RecommendationObject> recommendations = List.of(new RecommendationObject(userId, "Product", "Text"));

        Mockito.when(recommendationService.getListOfRecommendation(userId)).thenReturn(recommendations);

        mockMvc.perform(get("/recommendation/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['" + userId + "'][0].name").value("Product"));
    }
}
