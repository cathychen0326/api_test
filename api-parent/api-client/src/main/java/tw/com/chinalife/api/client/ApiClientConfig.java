package tw.com.chinalife.api.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import tw.com.chinalife.api.ApiConstants;
import tw.com.chinalife.api.ApiProperties;
import tw.com.chinalife.api.parser.ApiSignatureValidator;

@Configuration
@EnableConfigurationProperties(ApiProperties.class)
public class ApiClientConfig {

	private final ApiProperties apiProperties;

	public ApiClientConfig(ApiProperties apiProperties) {
		this.apiProperties = apiProperties;
	}

	@Bean
	@ConditionalOnMissingBean(ApiTokenSupplier.class)
	public ApiTokenSupplier channelTokenSupplier() {
		final String tokenToken = apiProperties.getToken();
		Assert.notNull(tokenToken, "solar.api.token 必須設定");
		return FixedApiTokenSupplier.of(tokenToken);
	}

	@Bean
	@ConditionalOnMissingBean
	public ApiClient apiClient(final ApiTokenSupplier apiTokenSupplier, MyInterceptor myInterceptor) {
		return ApiClient.builder(apiTokenSupplier).apiEndPoint(apiProperties.getEndPoint())
				.connectTimeout(apiProperties.getConnectTimeout()).readTimeout(apiProperties.getReadTimeout())
				.additionalInterceptors(Collections.singletonList(myInterceptor))
				.writeTimeout(apiProperties.getWriteTimeout()).build();
	}

	@Bean
	public MyInterceptor myInterceptor() {
		return new MyInterceptor(apiProperties.getSecretKey());
	}

	public static class MyInterceptor implements Interceptor {

		public MyInterceptor(String key) {
			this.key = key;
		}

		private String key;

		@Override
		public Response intercept(Chain chain) throws IOException {
			Request request = chain.request();
			byte[] body = bodyToByteArray(request);

			ApiSignatureValidator signatureValidator = new ApiSignatureValidator(
					key.getBytes(StandardCharsets.US_ASCII));

			byte[] signature = signatureValidator.generateSignature(body);
			String XApiSignature = Base64.getEncoder().encodeToString(signature);
			Request newRequest = request.newBuilder().addHeader(ApiConstants.SIGNATURE_HEADER_NAME, XApiSignature)
					.build();
			return chain.proceed(newRequest);
		}

		private static byte[] bodyToByteArray(final Request request) throws IOException {

			final Request copy = request.newBuilder().build();
			final Buffer buffer = new Buffer();
			copy.body().writeTo(buffer);
			return buffer.readByteArray();

		}

	}
}
