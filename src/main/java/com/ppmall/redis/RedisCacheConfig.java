package com.ppmall.redis;

import java.lang.reflect.Method;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class RedisCacheConfig {
	private volatile JedisConnectionFactory mJedisConnectionFactory;
	private volatile RedisTemplate<String, String> mRedisTemplate;
	private volatile RedisCacheManager mRedisCacheManager;

	public RedisCacheConfig() {
		super();
	}

	public RedisCacheConfig(JedisConnectionFactory mJedisConnectionFactory,
			RedisTemplate<String, String> mRedisTemplate, RedisCacheManager mRedisCacheManager) {
		super();
		this.mJedisConnectionFactory = mJedisConnectionFactory;
		this.mRedisTemplate = mRedisTemplate;
		this.mRedisCacheManager = mRedisCacheManager;
	}

	public JedisConnectionFactory redisConnectionFactory() {
		return mJedisConnectionFactory;
	}

	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
		return mRedisTemplate;
	}

	public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
		return mRedisCacheManager;
	}

	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				// 规定 本类名+方法名+参数名 为key
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName() + "_");
				sb.append(method.getName() + "_");
				for (Object obj : params) {
					sb.append(obj.toString() + ",");
				}
				return sb.toString();
			}
		};
	}
}
