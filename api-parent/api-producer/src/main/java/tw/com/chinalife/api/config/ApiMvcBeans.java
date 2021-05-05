package tw.com.chinalife.api.config;

import java.nio.charset.StandardCharsets;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import tw.com.chinalife.api.ApiProperties;
import tw.com.chinalife.api.interceptor.ApiHandleInterceptor;
import tw.com.chinalife.api.parser.ApiSignatureValidator;
import tw.com.chinalife.api.parser.WebhookParser;
import tw.com.chinalife.api.support.ApiHandlerArgumentProcessor;

@Component
@ConditionalOnWebApplication
@EnableConfigurationProperties(ApiProperties.class)
@Import({ ApiHandleInterceptor.class, ApiHandlerArgumentProcessor.class })
public class ApiMvcBeans {

	private ApiProperties properties;

	public ApiMvcBeans(ApiProperties apiProperties) {
		this.properties = apiProperties;
	}

	@Bean
	public ApiSignatureValidator apiSignatureValidator() {
		String key = properties.getSecretKey();
		return new ApiSignatureValidator(key.getBytes(StandardCharsets.US_ASCII));
	}

	/**
	 * Expose {@link WebhookParser} as {@link Bean}.
	 */
	@Bean
	public WebhookParser lineBotCallbackRequestParser(ApiSignatureValidator apiSignatureValidator) {
		return new WebhookParser(apiSignatureValidator);
	}
	
	
   @Bean
   public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
//	                .failFast(true)
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }
}
