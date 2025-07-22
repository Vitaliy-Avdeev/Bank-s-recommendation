package org.skypro.recommendService.controller;


import org.skypro.recommendService.service.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CacheController.class)
public class CacheControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ManagementService managementService;
    @MockitoBean
    private BuildProperties buildProperties;

    @org.junit.jupiter.api.Test
    public void clearCache_shouldCallService() throws Exception {
        mockMvc.perform(post("/management/clear-caches"))
                .andExpect(status().isOk());

        verify(managementService).clearCache();
    }

    @org.junit.jupiter.api.Test
    public void testGetServiceInfo() throws Exception {
        when(buildProperties.getName()).thenReturn("recommendService");
        when(buildProperties.getVersion()).thenReturn("0.0.1-SNAPSHOT");

        mockMvc.perform(get("/management/info"))
                .andExpect(status().isOk());
    }
}
