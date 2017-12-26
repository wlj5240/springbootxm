package com.itopener.demo.cache.redis.config;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.data.redis.cache.RedisCacheManager;

public class RedisCacheManagerCustomizer implements CacheManagerCustomizer<RedisCacheManager> {

	@Override
	public void customize(RedisCacheManager cacheManager) {
		// 默认过期时间，单位秒
		cacheManager.setDefaultExpiration(1000);
		cacheManager.setUsePrefix(false);
	}

}
