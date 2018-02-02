package com.itopener.zuul.ratelimiter.spring.boot.common.endpoint;

import java.util.Map;

import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.itopener.zuul.ratelimiter.spring.boot.common.entity.LimiterEntity;
import com.itopener.zuul.ratelimiter.spring.boot.common.entity.ZuulPathEntity;
import com.itopener.zuul.ratelimiter.spring.boot.common.support.ILimiterManager;
import com.itopener.zuul.ratelimiter.spring.boot.common.support.RateLimiterHandler;

@ConfigurationProperties(prefix = "endpoints.zuul.limiter.path")
public class ZuulPathRateLimiterEndpoint extends AbstractEndpoint<Map<String, Map<String, LimiterEntity>>> {
	
	private RateLimiterHandler rateLimiterHandler;
	
	private ILimiterManager limiterManager;

	public ZuulPathRateLimiterEndpoint(RateLimiterHandler rateLimiterHandler, ILimiterManager limiterManager) {
		super("zuul_limiter_path", false);
		this.rateLimiterHandler = rateLimiterHandler;
		this.limiterManager = limiterManager;
	}

	@Override
	public Map<String, Map<String, LimiterEntity>> invoke() {
		return rateLimiterHandler.getPathRateLimiterMap();
	}

	public void set(ZuulPathEntity zuulPathEntity) {
		limiterManager.set(zuulPathEntity);
		rateLimiterHandler.put(zuulPathEntity);
	}
	
	public LimiterEntity get(String id, String path) {
		Map<String, LimiterEntity> pathMap = rateLimiterHandler.getPathRateLimiterMap().get(id);
		return pathMap == null ? null : pathMap.get(path);
	}
	
	public Map<String, LimiterEntity> get(String id) {
		return rateLimiterHandler.getPathRateLimiterMap().get(id);
	}
}
