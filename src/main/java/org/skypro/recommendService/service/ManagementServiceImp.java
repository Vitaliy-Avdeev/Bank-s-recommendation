package org.skypro.recommendService.service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
/**
 * Сервис для управления системой, позволяет сбросить кеш рекомендаций.
 */
@Service
public class ManagementServiceImp implements ManagementService{
    /**
     * Метод очищает все за кешированные результаты, что означает обновление базы данных.
     * Spring автоматически очистит кеш благодаря аннотации.
     */

    @CacheEvict(value = "recommendation", allEntries = true)
    @Override
    public void clearCache() {
    }
}

