package cloud.simple.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cloud.conf.GlobalConf;
import cloud.conf.UserServiceFeignConfiguration;
import cloud.simple.model.User;

// 这边的name就是要调用的服务在eureka server中注册的serviceId，当使用zuulproxy时可以直接写成zuulproxy的serviceid
@FeignClient(name = GlobalConf.ZUUL_PROXY, configuration = UserServiceFeignConfiguration.class, fallback = FeignUserServiceFallback.class)
// @RibbonClient("hello") FeignClient自动会使用RibbonClient，所以不用再次声明
public interface IFeignUserService {

	@RequestMapping(value = "/api/user/{username}", method = RequestMethod.GET)
	List<User> users(@PathVariable("username") String userName);

	@RequestMapping(value = "/api/user/{username}", method = RequestMethod.POST)
	List<User> users(@PathVariable("username") String userName, @RequestParam("param") String param,
			@RequestHeader("some_header") String header, @RequestBody User user);

	@RequestMapping(value = "/api/user/session", method = RequestMethod.GET)
	String session();
}
