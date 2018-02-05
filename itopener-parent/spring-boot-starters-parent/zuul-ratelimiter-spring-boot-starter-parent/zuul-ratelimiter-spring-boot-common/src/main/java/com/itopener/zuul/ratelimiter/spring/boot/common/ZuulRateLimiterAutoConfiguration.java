package com.itopener.zuul.ratelimiter.spring.boot.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.itopener.zuul.ratelimiter.spring.boot.common.endpoint.ZuulIdRateLimiterEndpoint;
import com.itopener.zuul.ratelimiter.spring.boot.common.endpoint.ZuulIdRateLimiterMvcEndpoint;
import com.itopener.zuul.ratelimiter.spring.boot.common.endpoint.ZuulPathRateLimiterEndpoint;
import com.itopener.zuul.ratelimiter.spring.boot.common.endpoint.ZuulPathRateLimiterMvcEndpoint;
import com.itopener.zuul.ratelimiter.spring.boot.common.endpoint.ZuulRateLimiterEndpoint;
import com.itopener.zuul.ratelimiter.spring.boot.common.support.ILimiterManager;
import com.itopener.zuul.ratelimiter.spring.boot.common.support.RateLimiterHandler;
import com.itopener.zuul.ratelimiter.spring.boot.common.support.ZuulRateLimiterErrorFilter;
import com.itopener.zuul.ratelimiter.spring.boot.common.support.ZuulRateLimiterFilter;

/**
 * @author fuwei.deng
 * @date 2018年1月31日 下午5:48:11
 * @version 1.0.0
 */
@Configuration
@EnableConfigurationProperties(ZuulRateLimiterProperties.class)
public class ZuulRateLimiterAutoConfiguration {
	
	@Autowired
	private ZuulRateLimiterProperties zuulRateLimiterProperties;
	
	@Autowired
	private ZuulProperties zuulProperties;
	
	@Bean
	@ConditionalOnBean(RouteLocator.class)
	public ZuulRateLimiterFilter zuulRateLimiterFilter(RouteLocator routeLocator, RateLimiterHandler rateLimiterHandler) {
		return new ZuulRateLimiterFilter(routeLocator, zuulProperties, zuulRateLimiterProperties, rateLimiterHandler);
	}
	
	@Bean
	public ZuulRateLimiterErrorFilter zuulRateLimiterErrorFilter() {
		return new ZuulRateLimiterErrorFilter(zuulRateLimiterProperties);
	}
	
	@Bean
	@ConditionalOnBean(ILimiterManager.class)
	public RateLimiterHandler rateLimiterHandler(ILimiterManager limiterManager) {
		return new RateLimiterHandler(zuulRateLimiterProperties, limiterManager);
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(ILimiterManager.class)
	public ZuulIdRateLimiterEndpoint zuulIdRateLimiterEndpoint(RateLimiterHandler rateLimiterHandler, ILimiterManager limiterManager) {
		return new ZuulIdRateLimiterEndpoint(rateLimiterHandler, limiterManager);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public ZuulIdRateLimiterMvcEndpoint zuulIdRateLimiterMvcEndpoint(ZuulIdRateLimiterEndpoint zuulIdRateLimiterEndpoint) {
		return new ZuulIdRateLimiterMvcEndpoint(zuulIdRateLimiterEndpoint);
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(ILimiterManager.class)
	public ZuulPathRateLimiterEndpoint zuulPathRateLimiterEndpoint(RateLimiterHandler rateLimiterHandler, ILimiterManager limiterManager) {
		return new ZuulPathRateLimiterEndpoint(rateLimiterHandler, limiterManager);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public ZuulPathRateLimiterMvcEndpoint zuulPathRateLimiterMvcEndpoint(ZuulPathRateLimiterEndpoint zuulPathRateLimiterEndpoint) {
		return new ZuulPathRateLimiterMvcEndpoint(zuulPathRateLimiterEndpoint);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public ZuulRateLimiterEndpoint zuulRateLimiterEndpoint(RateLimiterHandler rateLimiterHandler) {
		return new ZuulRateLimiterEndpoint(rateLimiterHandler);
	}
}
