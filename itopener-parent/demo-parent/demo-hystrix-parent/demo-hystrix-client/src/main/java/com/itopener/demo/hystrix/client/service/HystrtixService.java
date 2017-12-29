package com.itopener.demo.hystrix.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.itopener.framework.ResultMap;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class HystrtixService {
	
	private final Logger logger = LoggerFactory.getLogger(HystrtixService.class);

	@HystrixCommand(fallbackMethod = "callFallback")
	public ResultMap call(long id){
		try {
			Thread.sleep(5000);
		} catch (Exception e){
			logger.error("sleep exception ", e);
		}
		return ResultMap.buildSuccess();
	}
	
	public ResultMap callFallback(long id){
		return ResultMap.buildFailed("hystrix fallback : " + id);
    }
}
