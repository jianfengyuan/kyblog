package com.kyblog.api.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.net.UnknownHostException;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-07 20:23
 **/

@Configuration
public class RedisConfig {
    @Bean(value = "myRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate( RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        // Object序列化配置
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // String序列化配置
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 设置template的序列化方式
        // key 采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash 的key采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value采用json的序列化方式
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash 的value采用String的序列化方式
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        // 确认设置properties
        template.afterPropertiesSet();
        return template;
    }
}
