package com.citi.skynet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citi.skynet.common.model.User;
import com.citi.skynet.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userSerivce;
	
	@RequestMapping("users")
	public List<User> getUsers(){	
		return userSerivce.selectUsers();
	}
}
