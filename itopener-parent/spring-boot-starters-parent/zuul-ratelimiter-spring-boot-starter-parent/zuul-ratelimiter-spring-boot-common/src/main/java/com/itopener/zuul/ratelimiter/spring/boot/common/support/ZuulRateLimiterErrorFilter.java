package com.itopener.zuul.ratelimiter.spring.boot.common.support;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.itopener.zuul.ratelimiter.spring.boot.common.ZuulRateLimiterProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**  
 * @author fuwei.deng
 * @date 2018年2月2日 下午3:24:49
 * @version 1.0.0
 */
public class ZuulRateLimiterErrorFilter extends ZuulFilter {

	private static final Logger logger = LoggerFactory.getLogger(ZuulRateLimiterErrorFilter.class);
	
	protected static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";

	private ZuulRateLimiterProperties zuulRateLimiterProperties;
	
	public ZuulRateLimiterErrorFilter(ZuulRateLimiterProperties zuulRateLimiterProperties) {
		super();
		this.zuulRateLimiterProperties = zuulRateLimiterProperties;
	}

	@Override
	public String filterType() {
		return FilterConstants.ERROR_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.SEND_ERROR_FILTER_ORDER - 1;
	}

	@Override
	public boolean shouldFilter() {
//		return false;
		RequestContext ctx = RequestContext.getCurrentContext();
		// only forward to errorPath if it hasn't been forwarded to already
		return ctx.getThrowable() != null
				&& !ctx.getBoolean(SEND_ERROR_FILTER_RAN, false)
				&& ctx.getThrowable().getCause() instanceof OverRateLimitException
				&& StringUtils.hasText(zuulRateLimiterProperties.getOverLimitPath());
	}

	@Override
	public Object run() {
		try {
			RequestContext ctx = RequestContext.getCurrentContext();
			OverRateLimitException exception = (OverRateLimitException) ctx.getThrowable().getCause();
			logger.warn("OverRateLimitException,statusCode:{},errorCause:{}", exception.getCode(), exception.getMessage());
			HttpServletRequest request = ctx.getRequest();
			
			request.setAttribute("javax.servlet.error.status_code", exception.getCode());
			request.setAttribute("javax.servlet.error.exception", exception);

			if (StringUtils.hasText(exception.getMessage())) {
				request.setAttribute("javax.servlet.error.message", exception.getMessage());
			}
			
			request.setAttribute("exception", exception);
			RequestDispatcher dispatcher = request.getRequestDispatcher(zuulRateLimiterProperties.getOverLimitPath());
			if (dispatcher != null) {
				ctx.set(SEND_ERROR_FILTER_RAN, true);
				if (!ctx.getResponse().isCommitted()) {
					dispatcher.forward(request, ctx.getResponse());
				}
			}
		}
		catch (Exception ex) {
			ReflectionUtils.rethrowRuntimeException(ex);
		}
		return null;
	}

}
