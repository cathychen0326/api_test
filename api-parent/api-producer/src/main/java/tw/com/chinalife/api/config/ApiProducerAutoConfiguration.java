package tw.com.chinalife.api.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import tw.com.chinalife.api.support.ApiHandlerSupport;

@Configuration
@AutoConfigureAfter(ApiWebMvcConfigurer.class)
@Import(ApiHandlerSupport.class)
public class ApiProducerAutoConfiguration {

}
