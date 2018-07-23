package com.citi.skynet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citi.skynet.common.model.User;
import com.citi.skynet.mapper.skynet.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	public List<User> selectUsers() {
		return userMapper.selectUsers();
	}
}
