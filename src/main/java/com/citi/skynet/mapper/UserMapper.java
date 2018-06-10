package com.citi.skynet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.citi.skynet.common.model.User;

@Mapper
public interface UserMapper {

	List<User> selectUsers();
}
