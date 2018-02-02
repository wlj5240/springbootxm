package com.itopener.zuul.ratelimiter.spring.boot.common.endpoint;

import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.boot.actuate.endpoint.mvc.HypermediaDisabled;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itopener.zuul.ratelimiter.spring.boot.common.entity.LimiterEntity;
import com.itopener.zuul.ratelimiter.spring.boot.common.entity.ZuulIdEntity;

@ConfigurationProperties(prefix = "endpoints.zuul.limiter.id")
public class ZuulIdRateLimiterMvcEndpoint extends EndpointMvcAdapter {
	
	private final ZuulIdRateLimiterEndpoint delegate;

	public ZuulIdRateLimiterMvcEndpoint(ZuulIdRateLimiterEndpoint delegate) {
		super(delegate);
		this.delegate = delegate;
	}

	@PostMapping("/zuul/limiter/id/{id:.*}")
	@ResponseBody
	@HypermediaDisabled
	public Object set(@PathVariable String id, ZuulIdEntity zuulIdEntity) {
		if (!this.delegate.isEnabled()) {
			// Shouldn't happen - MVC endpoint shouldn't be registered when delegate's disabled
			return getDisabledResponse();
		}
		try {
			zuulIdEntity.setId(id);
			this.delegate.set(zuulIdEntity);
			return ResponseEntity.ok().build();
		}
		catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
	
	@GetMapping("/zuul/limiter/id/{id:.*}")
	@ResponseBody
	@HypermediaDisabled
	public Object get(@PathVariable String id) {
		if (!this.delegate.isEnabled()) {
			// Shouldn't happen - MVC endpoint shouldn't be registered when delegate's disabled
			return getDisabledResponse();
		}
		try {
			LimiterEntity limiterEntity = this.delegate.get(id);
			return ResponseEntity.ok().body(limiterEntity);
		}
		catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
}
