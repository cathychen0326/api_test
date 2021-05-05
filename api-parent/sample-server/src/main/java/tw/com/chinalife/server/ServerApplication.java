package tw.com.chinalife.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}


	
//	@Bean
//	public FilterRegistrationBean<CharacterEncodingFilter> filterRegistrationBean() {
//	    FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new FilterRegistrationBean<CharacterEncodingFilter>();
//	    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//	    characterEncodingFilter.setForceEncoding(true);
//	    characterEncodingFilter.setEncoding("UTF-8");
//	    registrationBean.setFilter(characterEncodingFilter);
//	    return registrationBean;
//	}
}
