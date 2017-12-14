package com.itopener.ratelimiter.spring.boot.autoconfigure.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.util.concurrent.RateLimiter;
import com.itopener.ratelimiter.spring.boot.autoconfigure.annotations.GuavaRateLimiter;

public class RateLimiterHandler {

	private Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<String, RateLimiter>();
	
	public boolean tryAcquire(GuavaRateLimiter guavaRateLimiter) {
		RateLimiter rateLimiter = rateLimiterMap.get(guavaRateLimiter.value());
		if(rateLimiter == null) {
			rateLimiter = RateLimiter.create(guavaRateLimiter.permitsPerSecond());
			rateLimiterMap.put(guavaRateLimiter.value(), rateLimiter);
		}
		return rateLimiter.tryAcquire(guavaRateLimiter.permits(), guavaRateLimiter.timeout(), guavaRateLimiter.timeUnit());
	}

	public Map<String, RateLimiter> getRateLimiterMap() {
		return rateLimiterMap;
	}
	
}
