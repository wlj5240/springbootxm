package com.itopener.tools.redis.controller;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.itopener.tools.redis.Application;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = { "dev" })
public class SpringbootMockTest {
	
	private final Logger logger = LoggerFactory.getLogger(SpringbootMockTest.class);

	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	@Test
	public void test() {
		redisTemplate.opsForValue().set("redistest", "redistestvalue", 30, TimeUnit.SECONDS);
		String value = (String) redisTemplate.opsForValue().get("redistest");
		logger.info(value);
	}
}
