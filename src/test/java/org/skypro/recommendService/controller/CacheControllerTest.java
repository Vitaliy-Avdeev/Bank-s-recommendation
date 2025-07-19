package org.skypro.recommendService.controller;

import org.junit.Test;
import org.skypro.recommendService.service.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
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

    @Test
    public void clearCache_shouldCallService() throws Exception {
        mockMvc.perform(post("/management/clear-caches"))
                .andExpect(status().isOk());

        verify(managementService).clearCache();
    }
}
