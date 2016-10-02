package cloud.simple.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cloud.conf.GlobalConf;
import cloud.conf.UserServiceFeignConfiguration;
import cloud.simple.model.User;

// 这边的name就是要调用的服务在eureka server中注册的serviceId
@FeignClient(name = GlobalConf.USER_SERVICE_NAME, configuration = UserServiceFeignConfiguration.class, fallback = FeignUserServiceFallback.class)
// @RibbonClient("hello") FeignClient自动会使用RibbonClient，所以不用再次声明
public interface IFeignUserService {

	@RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
	List<User> users(@PathVariable("username") String userName);

}
