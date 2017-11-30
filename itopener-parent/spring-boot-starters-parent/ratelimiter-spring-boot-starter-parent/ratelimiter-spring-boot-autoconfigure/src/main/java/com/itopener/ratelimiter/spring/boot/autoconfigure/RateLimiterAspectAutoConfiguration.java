package com.itopener.ratelimiter.spring.boot.autoconfigure;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import com.google.common.util.concurrent.RateLimiter;
import com.itopener.ratelimiter.spring.boot.autoconfigure.annotations.GuavaRateLimiter;

@Aspect
@Configuration
@ConditionalOnClass(RateLimiter.class)
public class RateLimiterAspectAutoConfiguration {

	private final Logger logger = LoggerFactory.getLogger(RateLimiterAspectAutoConfiguration.class);
	
	private Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<String, RateLimiter>();
	
	@Pointcut("@annotation(com.itopener.ratelimiter.spring.boot.autoconfigure.annotations.GuavaRateLimiter)")
	private void rateLimiterPoint(){
		
	}
	
	@Around("rateLimiterPoint()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable{
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		GuavaRateLimiter guavaRateLimiter = method.getAnnotation(GuavaRateLimiter.class);
		
		if(!tryAcquire(guavaRateLimiter)) {
			logger.warn("[{}] has over limit", guavaRateLimiter.value());
			return null;
		}
		
		try {
			return pjp.proceed();
		} catch (Exception e) {
			logger.error("execute rate limiter method [" + guavaRateLimiter.value() + "] occured an exception", e);
		}
		return null;
	}
	
	public boolean tryAcquire(GuavaRateLimiter guavaRateLimiter) {
		RateLimiter rateLimiter = rateLimiterMap.get(guavaRateLimiter.value());
		if(rateLimiter == null) {
			rateLimiter = RateLimiter.create(guavaRateLimiter.permitsPerSecond());
			rateLimiterMap.put(guavaRateLimiter.value(), rateLimiter);
		}
		return rateLimiter.tryAcquire(guavaRateLimiter.permits(), guavaRateLimiter.timeout(), guavaRateLimiter.timeUnit());
	}
}
