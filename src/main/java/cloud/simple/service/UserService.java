/*
 * Copyright 2012-2020 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * @author lzhoumail@126.com/zhouli
 * Git http://git.oschina.net/zhou666/spring-cloud-7simple
 */
package cloud.simple.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cloud.simple.model.User;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class UserService {

	@Autowired
	RestTemplate restTemplate;

	final static String SERVICE_NAME = "user-service";
	
	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@SuppressWarnings({ "unchecked" })
	@HystrixCommand(fallbackMethod = "fallbackSearchAll")
	public List<User> readUserInfo() {
		return restTemplate.getForObject("http://" + SERVICE_NAME + "/user/notFeignUser", List.class);
	}

	@SuppressWarnings("unused")
	private List<User> fallbackSearchAll() {
		List<User> ls = new ArrayList<User>();
		User user = new User();
		user.setUsername("[not feign]TestHystrixCommand from simple ui");
		ls.add(user);
		return ls;
	}
}
