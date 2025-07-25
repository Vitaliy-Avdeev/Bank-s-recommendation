package org.skypro.recommendService.service;

import org.junit.jupiter.api.Test;

class ManagementServiceImpTest {

    @Test
    void testClearCache() {
        ManagementServiceImp service = new ManagementServiceImp();
        service.clearCache();
    }
}
