package com.itopener.zuul.ratelimiter.spring.boot.common.endpoint;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.itopener.zuul.ratelimiter.spring.boot.common.support.RateLimiterHandler;

@ConfigurationProperties(prefix = "endpoints.zuul.limiter")
public class ZuulRateLimiterEndpoint extends AbstractEndpoint<Map<String, ?>> {
	
	private RateLimiterHandler rateLimiterHandler;
	
	public ZuulRateLimiterEndpoint(RateLimiterHandler rateLimiterHandler) {
		super("zuul_limiter", false);
		this.rateLimiterHandler = rateLimiterHandler;
	}

	@Override
	public Map<String, ?> invoke() {
		rateLimiterHandler.generateRateLimiterMap();
		Map<String, Map<String, ?>> limiter = new HashMap<>();
		limiter.put("zuulIdRateLimiter", rateLimiterHandler.getZuulIdRateLimiterMap());
		limiter.put("zuulPathRateLimiter", rateLimiterHandler.getPathRateLimiterMap());
		return limiter;
	}
	
}
