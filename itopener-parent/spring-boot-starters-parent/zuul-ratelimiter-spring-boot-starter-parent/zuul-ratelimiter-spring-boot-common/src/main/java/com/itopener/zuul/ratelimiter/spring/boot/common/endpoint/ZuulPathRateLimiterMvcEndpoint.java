package com.itopener.zuul.ratelimiter.spring.boot.common.endpoint;

import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.boot.actuate.endpoint.mvc.HypermediaDisabled;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itopener.zuul.ratelimiter.spring.boot.common.entity.LimiterEntity;
import com.itopener.zuul.ratelimiter.spring.boot.common.entity.ZuulPathEntity;

@ConfigurationProperties(prefix = "endpoints.zuul.limiter.path")
public class ZuulPathRateLimiterMvcEndpoint extends EndpointMvcAdapter {
	
	private final ZuulPathRateLimiterEndpoint delegate;

	public ZuulPathRateLimiterMvcEndpoint(ZuulPathRateLimiterEndpoint delegate) {
		super(delegate);
		this.delegate = delegate;
	}

	@PostMapping("/zuul/limiter/path/{id:.*}")
	@ResponseBody
	@HypermediaDisabled
	public Object set(@PathVariable String id, ZuulPathEntity zuulPathEntity) {
		if (!this.delegate.isEnabled()) {
			// Shouldn't happen - MVC endpoint shouldn't be registered when delegate's disabled
			return getDisabledResponse();
		}
		try {
			zuulPathEntity.setZuulId(id);
			this.delegate.set(zuulPathEntity);
			return ResponseEntity.ok().build();
		}
		catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
	
	@GetMapping("/zuul/limiter/path/{id:.*}")
	@ResponseBody
	@HypermediaDisabled
	public Object get(@PathVariable String id, String path) {
		if (!this.delegate.isEnabled()) {
			// Shouldn't happen - MVC endpoint shouldn't be registered when delegate's disabled
			return getDisabledResponse();
		}
		try {
			if(StringUtils.hasText(path)) {
				LimiterEntity limiterEntity = this.delegate.get(id, path);
				return ResponseEntity.ok().body(limiterEntity);
			} else {
				return ResponseEntity.ok().body(this.delegate.get(id));
			}
		}
		catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
}
