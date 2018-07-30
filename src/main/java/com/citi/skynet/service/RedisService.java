package com.citi.skynet.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	public void add(String key, String value, Long time) {
		stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.DAYS);
	}

	// public void add(String key, List<User> users, Long time) {
	// Gson gson = new Gson();
	// String src = gson.toJson(users);
	// stringRedisTemplate.opsForValue().set(key, src, time, TimeUnit.MINUTES);
	// }

	public String get(String key) {
		String source = stringRedisTemplate.opsForValue().get(key);
		// if (!StringUtils.isEmpty(source)) {
		// return new Gson().fromJson(source, User.class);
		// }
		return source;
	}

	// public List<User> getUserList(String key) {
	// String source = stringRedisTemplate.opsForValue().get(key);
	// if (!StringUtils.isEmpty(source)) {
	// return new Gson().fromJson(source, new TypeToken<List<User>>() {
	// }.getType());
	// }
	// return null;
	// }

	public void delete(String key) {
		stringRedisTemplate.opsForValue().getOperations().delete(key);
	}
}
