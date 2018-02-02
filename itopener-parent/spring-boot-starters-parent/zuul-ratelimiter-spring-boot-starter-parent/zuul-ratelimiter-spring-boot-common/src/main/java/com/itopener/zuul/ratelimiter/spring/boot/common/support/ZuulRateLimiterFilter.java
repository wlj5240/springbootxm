package com.itopener.zuul.ratelimiter.spring.boot.common.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.web.util.UrlPathHelper;

import com.alibaba.fastjson.JSON;
import com.itopener.zuul.ratelimiter.spring.boot.common.ZuulRateLimiterProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**  
 * @author fuwei.deng
 * @date 2018年2月1日 下午2:16:52
 * @version 1.0.0
 */
public class ZuulRateLimiterFilter extends ZuulFilter {
	
	private final Logger logger = LoggerFactory.getLogger(ZuulRateLimiterFilter.class);
	
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	private RouteLocator routeLocator;
	
	private ZuulProperties properties;
	
	private ZuulRateLimiterProperties zuulRateLimiterProperties;
	
	private RateLimiterHandler rateLimiterHandler;
	
	public ZuulRateLimiterFilter(RouteLocator routeLocator, ZuulProperties properties, 
			ZuulRateLimiterProperties zuulRateLimiterProperties,
			RateLimiterHandler rateLimiterHandler) {
		super();
		this.routeLocator = routeLocator;
		this.properties = properties;
		this.zuulRateLimiterProperties = zuulRateLimiterProperties;
		this.urlPathHelper.setRemoveSemicolonContent(this.properties.isRemoveSemicolonContent());
		this.rateLimiterHandler = rateLimiterHandler;
	}

	@Override
	public boolean shouldFilter() {
		return zuulRateLimiterProperties.isLimiterSwitch();
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		final String requestURI = this.urlPathHelper.getPathWithinApplication(ctx.getRequest());
		Route route = this.routeLocator.getMatchingRoute(requestURI);
		logger.debug("ZuulRateLimiterFilter:{}", JSON.toJSONString(route));
		rateLimiterHandler.tryAcquire(route);
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		// 执行顺序在pre阶段最后执行
		return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
	}

}
