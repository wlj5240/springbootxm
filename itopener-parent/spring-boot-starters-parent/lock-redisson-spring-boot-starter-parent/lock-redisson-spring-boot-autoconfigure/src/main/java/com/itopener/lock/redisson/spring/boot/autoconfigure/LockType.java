package com.itopener.lock.redisson.spring.boot.autoconfigure;

/**  
 * @author fuwei.deng
 * @date 2018年1月9日 上午11:25:59
 * @version 1.0.0
 */
public enum LockType {

	REENTRANT_LOCK,
	
	FAIR_LOCK,
	
	READ_LOCK,
	
	WRITE_LOCK;
}
