package com.itopener.demo.lock.redisson.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.itopener.lock.redisson.spring.boot.autoconfigure.LockAction;

@Service
public class RedissonLockService {

	private final Logger logger = LoggerFactory.getLogger(RedissonLockService.class);
	
	@LockAction("'updateKey'")
	public void update(String key){
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			logger.error("exp", e);
		}
	}
	
}
