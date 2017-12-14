package com.itopener.ratelimiter.spring.boot.autoconfigure;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.util.concurrent.RateLimiter;
import com.itopener.ratelimiter.spring.boot.autoconfigure.endpoint.RateLimiterEndpoint;
import com.itopener.ratelimiter.spring.boot.autoconfigure.endpoint.RateLimiterMvcEndpoint;
import com.itopener.ratelimiter.spring.boot.autoconfigure.support.RateLimiterHandler;

@Aspect
@Configuration
@ConditionalOnClass(RateLimiter.class)
public class RateLimiterAutoConfiguration {

	@Bean
	public RateLimiterHandler rateLimiterHandler() {
		return new RateLimiterHandler();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public RateLimiterEndpoint rateLimiterEndpoint(RateLimiterHandler rateLimiterHandler) {
		return new RateLimiterEndpoint(rateLimiterHandler);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public RateLimiterMvcEndpoint rateLimiterMvcEndpoint(RateLimiterEndpoint rateLimiterEndpoint) {
		return new RateLimiterMvcEndpoint(rateLimiterEndpoint);
	}
}
