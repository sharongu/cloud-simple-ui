package cloud.conf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/*
 * The UserServiceFeignConfiguration has to be @Configuration but take care that it is not in a @ComponentScan 
 for the main application context, otherwise it will be used for every @FeignClient. 
 If you use @ComponentScan (or @SpringBootApplication) you need to take steps to avoid it being included 
 (for instance put it in a separate, non-overlapping package, or specify the packages to scan explicitly 
 in the @ComponentScan).
 */

@Configuration
public class UserServiceFeignConfiguration {

	/*
	 * If Hystrix is on the classpath, by default Feign will wrap all methods with a circuit breaker. Returning a com.netflix.hystrix.HystrixCommand is also available. This lets you use reactive
	 * patterns (with a call to .toObservable() or .observe() or asynchronous use (with a call to .queue()).
	 * 
	 * To disable Hystrix support for Feign, set feign.hystrix.enabled=false.
	 * 
	 * To disable Hystrix support on a per-client basis create a vanilla Feign.Builder with the "prototype" scope
	 * 
	 * 覆盖默认的Feign.Builder以后就没法用FeignClient里面的Hystrix的Fallback类了
	 */
	// @Bean
	// @Scope("prototype")
	// public Feign.Builder feignBuilder() {
	// return Feign.builder();
	// }

	@Bean
	public Logger.Level feignLogger() {
		return Logger.Level.BASIC;
	}

	private static final int FIVE_SECONDS = 5000;

	@Bean
	public Request.Options options() {
		return new Request.Options(FIVE_SECONDS, FIVE_SECONDS);
	}

	@Bean
	public RequestInterceptor requestSessionInterceptor() {
		return new RequestInterceptor() {

			@Override
			public void apply(RequestTemplate template) {
				HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
						.getRequest();
				HttpSession session = req.getSession(false);
				if (session != null) // 在FiegnClient调用微服务时传递sessionid，确保session在各个微服务之间的调用中正确传递，实际上后端服务应该尽量不要依赖session
					template.header("Cookie", "SESSION=" + req.getSession().getId());
			}

		};
	}

}
