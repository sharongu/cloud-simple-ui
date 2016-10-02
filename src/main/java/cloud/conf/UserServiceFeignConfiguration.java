package cloud.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import feign.Feign;
import feign.Logger;
import feign.Request;

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
	@Bean
	@Scope("prototype")
	public Feign.Builder feignBuilder() {
		return Feign.builder();
	}

	@Bean
	public Logger.Level feignLogger() {
		return Logger.Level.FULL;
	}

	private static final int FIVE_SECONDS = 5000;

	@Bean
	public Request.Options options() {
		return new Request.Options(FIVE_SECONDS, FIVE_SECONDS);
	}

}