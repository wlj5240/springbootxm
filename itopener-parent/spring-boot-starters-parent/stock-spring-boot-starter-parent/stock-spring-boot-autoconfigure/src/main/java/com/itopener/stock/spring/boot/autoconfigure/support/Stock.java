package com.itopener.stock.spring.boot.autoconfigure.support;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

/**  
 * @author fuwei.deng
 * @date 2018年2月6日 下午5:38:33
 * @version 1.0.0
 */
public class Stock {
	
	private final Logger logger = LoggerFactory.getLogger(Stock.class);
	
	private RedisTemplate<Object, Object> redisTemplate;
	
	private ThreadLocal<String> lockFlag = new ThreadLocal<String>();

	public static final String STOCK_LUA;
	
	public static final String UNLOCK_LUA;

    public Stock(RedisTemplate<Object, Object> redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
	}

	static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }
	
	static {
		/**
		 * @desc 扣减库存Lua脚本
		 * @params 库存key
		 * @return
		 * 		-2：库存不足
		 * 		-1:库存不限
		 * 		-3:库存未初始化
		 * 		其他:剩余库存（扣减之后剩余的库存）
		 * 
		 * 返回结果>-2表示扣减库存成功
		 */
		StringBuilder sb = new StringBuilder();
        sb.append("if (redis.call('exists', KEYS[1]) == 1) then");
        sb.append("    local stock = tonumber(redis.call('get', KEYS[1]));");
        // 不限库存
        sb.append("    if (stock == -1) then");
        sb.append("        return -1;");
        sb.append("    end;");
        // 如果库存足够，执行减库存操作并返回减之后的库存
        sb.append("    local deductStock = tonumber(ARGV[1]);");
        sb.append("    if (stock >= deductStock) then");
        sb.append("        redis.call('incrby', KEYS[1], (0 - deductStock));");
        sb.append("        return stock - deductStock;");
        sb.append("    end;");
        // 库存不足
        sb.append("    return -2;");
        sb.append("end;");
        // 库存未初始化
        sb.append("return -3;");
        STOCK_LUA = sb.toString();
	}
	
	/**
	 * 方法stock的功能描述:扣减并返回库存
	 * 方法改动记录:
	 *
	 * @param key 库存在redis中的key
	 * @param deductStock 扣减库存
	 * @param expire 库存过期时间
	 * @param stockCallback 当库存未初始化时获取库存的方法
	 * @return 
	 * 		-2：库存不足
	 * 		-1:库存不限
	 * 		-4:库存初始化失败
	 * 		>-2:扣减库存成功
	 * 		其他:剩余库存（扣减之后剩余的库存）
	 * @version : 1.0
	 * @time	: 2018年1月23日
	 * @author	: fuwei.deng
	 */
	public long stock(String key, long deductStock, long expire, IStockCallback stockCallback) {
		long stock = stock(key, deductStock);
		if(stock == -3) {
			try {
				// 分布式锁的key不能与库存的key相同，否则加锁的时候会把库存的值替换掉，导致脚本获取到的值类型不匹配
				if(lock("stocklock:" + key, expire)) {
					// 双重验证，避免并发时重复回源到数据库
					stock = stock(key, deductStock);
					if(stock == -3) {
						final long srcStock = stockCallback.getStock(key);
						
						String result = redisTemplate.execute(new RedisCallback<String>() {
							@Override
							public String doInRedis(RedisConnection connection) throws DataAccessException {
								JedisCommands commands = (JedisCommands) connection.getNativeConnection();
								return commands.set(key, String.valueOf(srcStock), "NX", "PX", expire);
							}
						});
						
						return !StringUtils.isEmpty(result) ? stock(key, deductStock) : -4;
					}
				}
			} finally {
				releaseLock("stocklock:" + key);
			}
			
		}
		return stock;
	}
	
	private long stock(String key, long deductStock) {
		List<String> keys = new ArrayList<String>();
		keys.add(key);
		List<String> args = new ArrayList<String>();
		args.add(String.valueOf(deductStock));
		long result = redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				Object nativeConnection = connection.getNativeConnection();
				// 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
				// 集群模式
				if (nativeConnection instanceof JedisCluster) {
					return (Long) ((JedisCluster) nativeConnection).eval(STOCK_LUA, keys, args);
				}

				// 单机模式
				else if (nativeConnection instanceof Jedis) {
					return (Long) ((Jedis) nativeConnection).eval(STOCK_LUA, keys, args);
				}
				return -1L;
			}
		});
		return result;
	}
	
	private boolean lock(String key, long expire) {
		try {
			String result = redisTemplate.execute(new RedisCallback<String>() {
				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {
					JedisCommands commands = (JedisCommands) connection.getNativeConnection();
					String uuid = UUID.randomUUID().toString();
					lockFlag.set(uuid);
					return commands.set(key, uuid, "NX", "PX", expire);
				}
			});
			return !StringUtils.isEmpty(result);
		} catch (Exception e) {
			logger.error("set redis occured an exception", e);
		}
		return false;
	}
	
	private boolean releaseLock(String key) {
		// 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
		try {
			List<String> keys = new ArrayList<String>();
			keys.add(key);
			List<String> args = new ArrayList<String>();
			args.add(lockFlag.get());

			// 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
			// spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
			
			Long result = redisTemplate.execute(new RedisCallback<Long>() {
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					Object nativeConnection = connection.getNativeConnection();
					// 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
					// 集群模式
					if (nativeConnection instanceof JedisCluster) {
						return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, args);
					}

					// 单机模式
					else if (nativeConnection instanceof Jedis) {
						return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, args);
					}
					return 0L;
				}
			});
			
			return result != null && result > 0;
		} catch (Exception e) {
			logger.error("release lock occured an exception", e);
		}
		return false;
	}
	
}
