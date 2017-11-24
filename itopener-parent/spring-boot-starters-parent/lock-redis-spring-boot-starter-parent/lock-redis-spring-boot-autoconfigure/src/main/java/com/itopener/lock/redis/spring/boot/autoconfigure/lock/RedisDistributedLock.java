package com.itopener.lock.redis.spring.boot.autoconfigure.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import redis.clients.jedis.JedisCommands;

/**
 * @author fuwei.deng
 * @date 2017年6月14日 下午3:11:14
 * @version 1.0.0
 */
public class RedisDistributedLock extends AbstractDistributedLock {
	
	private final Logger logger = LoggerFactory.getLogger(RedisDistributedLock.class);
	
	private RedisTemplate<Object, Object> redisTemplate;

	public RedisDistributedLock(RedisTemplate<Object, Object> redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
	}

	@Override
	public synchronized boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
		boolean result = setRedis(key, expire);
		// 如果获取锁失败，按照传入的重试次数进行重试
		while((!result) && retryTimes-- > 0){
			try {
				logger.info("lock failed, retrying..." + retryTimes);
				Thread.sleep(sleepMillis);
			} catch (InterruptedException e) {
				return false;
			}
			result = setRedis(key, expire);
		}
		return result;
	}
	
	private boolean setRedis(String key, long expire) {
		String result = redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				JedisCommands commands = (JedisCommands) connection.getNativeConnection();
				return commands.set(key, String.valueOf(System.currentTimeMillis()), "NX", "PX", expire);
			}
		});
		return !StringUtils.isEmpty(result);
	}

	@Override
	public boolean releaseLock(String key) {
		// TODO 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				JedisCommands commands = (JedisCommands) connection.getNativeConnection();
				return commands.del(key);
			}
		});
		return result != null && result > 0;
	}

}
