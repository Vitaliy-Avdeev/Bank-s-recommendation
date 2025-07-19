package org.skypro.recommendService.service;

import org.springframework.cache.annotation.CacheEvict;

public interface ManagementService {
    // Метод сброса кеша рекомендаций.
    @CacheEvict(value = "recommendation", allEntries = true)
    void clearCache();
}

