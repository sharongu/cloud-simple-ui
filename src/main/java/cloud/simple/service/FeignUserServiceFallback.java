package cloud.simple.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;

import cloud.simple.model.User;

/*There is a limitation with the implementation of fallbacks in Feign and how Hystrix fallbacks work. 
 Fallbacks are currently not supported for methods that return com.netflix.hystrix.HystrixCommand and rx.Observable.*/

@Configuration
public class FeignUserServiceFallback implements IFeignUserService {

	@Override
	public List<User> users(String userName) {
		List<User> ls = new ArrayList<User>();
		User u = new User();
		u.setUsername("[feign]TestHystrixFallback from simple ui");
		ls.add(u);
		return ls;
	}

}
