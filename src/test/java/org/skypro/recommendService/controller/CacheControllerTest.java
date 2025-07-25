package org.skypro.recommendService.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skypro.recommendService.service.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CacheController.class)
class CacheControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ManagementService managementService;

    @MockitoBean
    private BuildProperties buildProperties;

    @Test
    void testClearCaches() throws Exception {
        mockMvc.perform(post("/management/clear-caches"))
                .andExpect(status().isOk());
        Mockito.verify(managementService).clearCache();
    }

    @Test
    void testGetServiceInfo() throws Exception {
        Mockito.when(buildProperties.getName()).thenReturn("TestApp");
        Mockito.when(buildProperties.getVersion()).thenReturn("1.0");

        mockMvc.perform(get("/management/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("TestApp"))
                .andExpect(jsonPath("$[0].version").value("1.0"));
    }
}
