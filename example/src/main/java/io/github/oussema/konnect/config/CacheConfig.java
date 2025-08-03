package io.github.oussema.konnect.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * Cache configuration
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Caffeine cache manager (default)
     * Local in-memory cache with high performance
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = "spring.cache.type", havingValue = "caffeine", matchIfMissing = true)
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .recordStats());

        // Pre-configure caches
        cacheManager.setCacheNames(java.util.Arrays.asList("processedWebhooks", "paymentDetails", "rateLimiting"));

        return cacheManager;
    }


}
