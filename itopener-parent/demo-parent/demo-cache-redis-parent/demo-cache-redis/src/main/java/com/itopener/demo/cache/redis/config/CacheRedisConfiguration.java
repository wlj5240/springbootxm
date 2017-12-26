package com.itopener.demo.cache.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fuwei.deng
 * @date 2017年12月22日 上午10:24:54
 * @version 1.0.0
 */
@Configuration
public class CacheRedisConfiguration {
	
	@Bean
	public RedisCacheManagerCustomizer redisCacheManagerCustomizer() {
		return new RedisCacheManagerCustomizer();
	}
}
