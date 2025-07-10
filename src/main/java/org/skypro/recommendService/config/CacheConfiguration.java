package org.skypro.recommendService.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfiguration {
    @Bean
    public Cache<String, Boolean> queryResultCache() {
        return Caffeine.newBuilder()
                .maximumSize(100)          // Максимум 100 записей или сколько надо?
                .expireAfterWrite(5, TimeUnit.MINUTES)  // Удалить через 5 минут
                .build();
    }
}
