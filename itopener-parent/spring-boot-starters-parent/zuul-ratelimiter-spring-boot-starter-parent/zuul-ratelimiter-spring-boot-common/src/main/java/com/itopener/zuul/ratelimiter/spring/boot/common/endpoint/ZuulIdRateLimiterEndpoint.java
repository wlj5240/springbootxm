package com.itopener.zuul.ratelimiter.spring.boot.common.endpoint;

import java.util.Map;

import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.itopener.zuul.ratelimiter.spring.boot.common.entity.LimiterEntity;
import com.itopener.zuul.ratelimiter.spring.boot.common.entity.ZuulIdEntity;
import com.itopener.zuul.ratelimiter.spring.boot.common.support.ILimiterManager;
import com.itopener.zuul.ratelimiter.spring.boot.common.support.RateLimiterHandler;

/**
 * @description 使用zuul id配置限流的endpoint
 * @author fuwei.deng
 * @date 2018年2月5日 上午9:45:37
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "endpoints.zuul.limiter.id")
public class ZuulIdRateLimiterEndpoint extends AbstractEndpoint<Map<String, LimiterEntity>> {
	
	private RateLimiterHandler rateLimiterHandler;
	
	private ILimiterManager limiterManager;

	public ZuulIdRateLimiterEndpoint(RateLimiterHandler rateLimiterHandler, ILimiterManager limiterManager) {
		super("zuul_limiter_id", false);
		this.rateLimiterHandler = rateLimiterHandler;
		this.limiterManager = limiterManager;
	}

	@Override
	public Map<String, LimiterEntity> invoke() {
		return rateLimiterHandler.getZuulIdRateLimiterMap();
	}

	public void set(ZuulIdEntity zuulIdEntity) {
		limiterManager.set(zuulIdEntity);
		rateLimiterHandler.put(zuulIdEntity);
	}
	
	public LimiterEntity get(String id) {
		return rateLimiterHandler.getZuulIdRateLimiterMap().get(id);
	}
	
}
