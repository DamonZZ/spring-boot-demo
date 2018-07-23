package com.citi.skynet.mapper.skynet;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.citi.skynet.common.model.User;

@Mapper
public interface UserMapper {

	@Select("SELECT id,name,hostname,ipaddress FROM user")
	@Results({ @Result(column = "id", property = "id"), @Result(column = "name", property = "name"),
			@Result(column = "hostname", property = "hostname"),
			@Result(column = "ipaddress", property = "ipaddress") })
	List<User> selectUsers();
}
