package com.ppmall.redis;

import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;

public class CustomizedRedisCacheManager extends RedisCacheManager {

	public CustomizedRedisCacheManager(RedisOperations redisOperations) {
		super(redisOperations);
		// TODO Auto-generated constructor stub
	}

}
