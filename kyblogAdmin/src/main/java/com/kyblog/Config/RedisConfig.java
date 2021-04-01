package com.kyblog.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // 設置key的序列化方式
        redisTemplate.setKeySerializer(RedisSerializer.string());
        // 設置value的序列化方式
        redisTemplate.setValueSerializer(RedisSerializer.json());
        // value本身是hash 因此要設置hash的序列化方式
        // 設置hash的key的序列化方式
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // 設置hash的value的序列化方式
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
