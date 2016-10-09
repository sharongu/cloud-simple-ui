/*
 * Copyright 2012-2020 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * @author lzhoumail@126.com/zhouli
 * Git http://git.oschina.net/zhou666/spring-cloud-7simple
 */

package cloud.simple.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.simple.model.User;
import cloud.simple.service.IFeignUserService;
import cloud.simple.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "/ui/users")
	public ResponseEntity<List<User>> readUserInfo() {
		List<User> users = userService.readUserInfo();
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	// 使用FeignClient的方式来调用服务
	@Autowired
	IFeignUserService client;

	@RequestMapping(value = "/ui/usersByFeign")
	public ResponseEntity<List<User>> readUserInfoByFeign() {
		List<User> users = client.users("feignUser");
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
}
