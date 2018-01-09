package com.itopener.demo.lock.redisson.controller;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itopener.demo.lock.redisson.service.RedissonLockService;
import com.itopener.demo.lock.redisson.vo.UserVO;
import com.itopener.framework.ResultMap;

@RestController
@RequestMapping("redissonlock")
public class RedissonLockController {
	
	private final Logger logger = LoggerFactory.getLogger(RedissonLockController.class);

	@Resource
	private RedissonLockService redisLockService;
	
	@Resource
	private RedissonClient redissonClient;
	
	@RequestMapping("aspect")
	public ResultMap lockAspect(){
		for(int i=0; i<10; i++){
			new RedisLockAspectThread().start();
		}
		return ResultMap.buildSuccess();
	}
	
	@RequestMapping("lock")
	public ResultMap lock(){
		for(int i=0; i<10; i++){
			new RedisLockThread().start();
		}
		return ResultMap.buildSuccess();
	}
	
	class RedisLockThread extends Thread {

		@Override
		public void run() {
			String key = "lockKey";
			RLock lock = redissonClient.getLock(key);
			try {
				boolean result = lock.tryLock(3000, 30000, TimeUnit.MILLISECONDS);
				logger.info(result ? "get lock success : " + key : "get lock failed : " + key);
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.error("exp", e);
			} finally {
				lock.unlock();
				logger.info("release lock : " + key);
			}
		}
	}
	
	class RedisLockAspectThread extends Thread {
		
		@Override
		public void run() {
//			String key = "lockKey2";
//			redisLockService.update(key);
			
			UserVO userVO = new UserVO();
			userVO.setId(20L);
			userVO.setName("fuwei.deng");
			redisLockService.update(userVO);
		}
	}
}
