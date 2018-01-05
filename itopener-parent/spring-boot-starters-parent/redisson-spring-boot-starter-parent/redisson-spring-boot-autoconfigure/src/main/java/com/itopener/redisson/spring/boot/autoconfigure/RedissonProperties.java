package com.itopener.redisson.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.itopener.redisson.spring.boot.autoconfigure.config.Config;
import com.itopener.redisson.spring.boot.autoconfigure.config.ConfigFile;

/**
 * @author fuwei.deng
 * @date 2018年1月3日 下午2:12:47
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "spring.redisson")
public class RedissonProperties {
	
	private Config config;
	
    private ConfigFile configFile = new ConfigFile();

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public ConfigFile getConfigFile() {
		return configFile;
	}

	public void setConfigFile(ConfigFile configFile) {
		this.configFile = configFile;
	}
	
}
