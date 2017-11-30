package com.itopener.demo.ratelimiter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itopener.framework.ResultMap;
import com.itopener.ratelimiter.spring.boot.autoconfigure.annotations.GuavaRateLimiter;

@RestController
@RequestMapping("ratelimiter")
public class RateLimiterController {
	
	private final Logger logger = LoggerFactory.getLogger(RateLimiterController.class);

	@GetMapping("index")
	@GuavaRateLimiter(permitsPerSecond = 1)
	public ResultMap index(){
		logger.info("execute RateLimiterController method : index()");
		return ResultMap.buildSuccess();
	}
}
