package com.itopener.cache.redis.caffeine.spring.boot.autoconfigure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**  
 * @author fuwei.deng
 * @date 2018年1月29日 上午11:32:15
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "spring.cache.multi")
public class CacheRedisCaffeineProperties {
	
	private Set<String> cacheNames = new HashSet<>();
	
	private boolean cacheNullValues = true;
	
	private boolean dynamic = true;
	
	private String cachePrefix;
	
	private Redis redis = new Redis();
	
	private Caffeine caffeine = new Caffeine();

	public class Redis {
		
		private long defaultExpiration = 0;
		
		private Map<String, Long> expires = new HashMap<>();
		
		private String topic = "cache:redis:caffeine:topic";

		public long getDefaultExpiration() {
			return defaultExpiration;
		}

		public void setDefaultExpiration(long defaultExpiration) {
			this.defaultExpiration = defaultExpiration;
		}

		public Map<String, Long> getExpires() {
			return expires;
		}

		public void setExpires(Map<String, Long> expires) {
			this.expires = expires;
		}

		public String getTopic() {
			return topic;
		}

		public void setTopic(String topic) {
			this.topic = topic;
		}
		
	}
	
	public class Caffeine {
		
		private long expireAfterAccess;
		
		private long expireAfterWrite;
		
		private long refreshAfterWrite;
		
		private int initialCapacity;
		
		private long maximumSize;
		
		private long maximumWeight;
		
		public long getExpireAfterAccess() {
			return expireAfterAccess;
		}

		public void setExpireAfterAccess(long expireAfterAccess) {
			this.expireAfterAccess = expireAfterAccess;
		}

		public long getExpireAfterWrite() {
			return expireAfterWrite;
		}

		public void setExpireAfterWrite(long expireAfterWrite) {
			this.expireAfterWrite = expireAfterWrite;
		}

		public long getRefreshAfterWrite() {
			return refreshAfterWrite;
		}

		public void setRefreshAfterWrite(long refreshAfterWrite) {
			this.refreshAfterWrite = refreshAfterWrite;
		}

		public int getInitialCapacity() {
			return initialCapacity;
		}

		public void setInitialCapacity(int initialCapacity) {
			this.initialCapacity = initialCapacity;
		}

		public long getMaximumSize() {
			return maximumSize;
		}

		public void setMaximumSize(long maximumSize) {
			this.maximumSize = maximumSize;
		}

		public long getMaximumWeight() {
			return maximumWeight;
		}

		public void setMaximumWeight(long maximumWeight) {
			this.maximumWeight = maximumWeight;
		}
		
	}

	public Set<String> getCacheNames() {
		return cacheNames;
	}

	public void setCacheNames(Set<String> cacheNames) {
		this.cacheNames = cacheNames;
	}

	public boolean isCacheNullValues() {
		return cacheNullValues;
	}

	public void setCacheNullValues(boolean cacheNullValues) {
		this.cacheNullValues = cacheNullValues;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public String getCachePrefix() {
		return cachePrefix;
	}

	public void setCachePrefix(String cachePrefix) {
		this.cachePrefix = cachePrefix;
	}

	public Redis getRedis() {
		return redis;
	}

	public void setRedis(Redis redis) {
		this.redis = redis;
	}

	public Caffeine getCaffeine() {
		return caffeine;
	}

	public void setCaffeine(Caffeine caffeine) {
		this.caffeine = caffeine;
	}

}
