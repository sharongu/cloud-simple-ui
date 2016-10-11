/*
 * Copyright 2012-2020 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * @author lzhoumail@126.com/zhouli
 * Git http://git.oschina.net/zhou666/spring-cloud-7simple
 */

package cloud.simple.web;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.simple.model.User;
import cloud.simple.service.IFeignUserService;
import cloud.simple.service.UserService;

@RestController
@RequestMapping(value = "/ui")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserService userService;

	@RequestMapping(value = "/users")
	public ResponseEntity<List<User>> readUserInfo() {
		List<User> users = userService.readUserInfo();
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	// 使用FeignClient的方式来调用服务
	@Autowired
	IFeignUserService client;

	@RequestMapping(value = "/usersByFeign")
	public ResponseEntity<List<User>> readUserInfoByFeign() {
		List<User> users = client.users("feignUser");
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	@RequestMapping("/session")
	public String testSession(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append("simple-ui里面的sessionid=" + request.getSession().getId());
		sb.append("<br>当前值testname=" + request.getSession().getAttribute("testname") + ",result=" + request.getSession().getAttribute("result"));
		request.getSession().setAttribute("testname", "simple-ui");
		sb.append("，<br>将testname设置为：simple-ui。<br>");
		sb.append("调用user-service后得到的结果：<br>");
		sb.append(client.session());
		// 注意：虽然user-service修改了testname并设置了result，但是在这边直接读取testname还是旧的值，result为null，试过延时5秒钟还是读不到新的值，只有当一个新的request
		// 发起时才能读取到redis里面的新值，猜测是因为对于每个request来说，第一次通过getSession获取session时就会从redis获取到所有的属性并缓存，而不是每次getAttribute时
		// 都从redis获取导致的
		sb.append("此时再次读取testname=" + request.getSession().getAttribute("testname"));
		sb.append("<br>读取由user-service设置的result=" + request.getSession().getAttribute("result"));
		return sb.toString();
	}

	@RequestMapping(value = "/log")
	public String testLog(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		Cookie[] cookies = request.getCookies();
		sb.append("显示Cookies:\n");
		if (cookies != null)
			for (Cookie cookie : cookies)
				sb.append((cookie.getName() + "=" + cookie.getValue()) + "\n");
		sb.append("显示Headers:\n");
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = "";
			Enumeration<String> values = request.getHeaders(name);
			while (values.hasMoreElements())
				value += values.nextElement() + ",";
			sb.append(name + "=" + value + "\n");
		}
		String logStr = sb.toString();
		logger.info(logStr);
		return logStr;
	}

}
