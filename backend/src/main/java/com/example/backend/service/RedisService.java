package com.example.backend.service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveToCache(String shortCode, String url) {
        redisTemplate.opsForValue().set(shortCode, url, 1, TimeUnit.DAYS);
    }

    public String getFromCache(String shortCode) {
        return redisTemplate.opsForValue().get(shortCode);
    }
    public void deleteFromCache(String shortCode) {
        redisTemplate.delete(shortCode);
    }
}

