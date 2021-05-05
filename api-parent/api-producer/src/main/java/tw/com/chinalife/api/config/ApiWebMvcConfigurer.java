package tw.com.chinalife.api.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import tw.com.chinalife.api.interceptor.ApiHandleInterceptor;
import tw.com.chinalife.api.support.ApiHandlerArgumentProcessor;

@Configuration
@Import(ApiMvcBeans.class)
@ConditionalOnWebApplication
public class ApiWebMvcConfigurer implements WebMvcConfigurer {
	@Autowired
	private ApiHandleInterceptor apiHandleInterceptor;
	@Autowired
	private ApiHandlerArgumentProcessor apiHandlerArgumentProcessor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(apiHandleInterceptor);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(apiHandlerArgumentProcessor);
	}
}
