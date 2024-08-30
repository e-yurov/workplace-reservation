package com.rc.mentorship.workplace_reservation.config;

import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
public class RedisConfig {
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                        SerializationPair.fromSerializer(
                                new Jackson2JsonRedisSerializer<>(UserResponse.class)
                        )
                );
    }
}
