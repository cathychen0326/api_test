package tw.com.chinalife.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}
//	@Bean
//	public FilterRegistrationBean<CharacterEncodingFilter> filterRegistrationBean() {
//	    FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new FilterRegistrationBean<>();
//	    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//	    characterEncodingFilter.setForceEncoding(true);
//	    characterEncodingFilter.setEncoding("UTF-8");
//	    registrationBean.setFilter(characterEncodingFilter);
//	    return registrationBean;
//	}
}
