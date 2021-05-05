package tw.com.chinalife.api.client;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.PackagePrivate;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import tw.com.chinalife.api.ApiConstants;
import tw.com.chinalife.api.objectmapper.ModelObjectMapper;

@ToString
@Accessors(fluent = true)
public class ApiClientBuilder {
	private static final ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();

	/**
	 * Use {@link ApiClient#builder(String)} to create instance.
	 *
	 * @see ApiClient#builder(String)
	 * @see ApiClient#builder(ApiTokenSupplier)
	 */
	@PackagePrivate
	ApiClientBuilder() {
	}

	/**
	 * API Endpoint.
	 *
	 * <p>
	 * Default value = "http://localhost:8080/".
	 */
	private URI apiEndPoint = ApiConstants.DEFAULT_API_END_POINT;

	public ApiClientBuilder apiEndPoint(@NonNull URI apiEndPoint) {
		this.apiEndPoint = requireNonNull(apiEndPoint, "apiEndPoint");
		return this;
	}

	/**
	 * Connection timeout.
	 *
	 * <p>
	 * Default value = {@value ApiConstants#DEFAULT_CONNECT_TIMEOUT_MILLIS}ms.
	 */
	@Setter
	private long connectTimeout = ApiConstants.DEFAULT_CONNECT_TIMEOUT_MILLIS;

	/**
	 * Connection timeout.
	 *
	 * <p>
	 * Default value = {@value ApiConstants#DEFAULT_READ_TIMEOUT_MILLIS}ms.
	 */
	@Setter
	private long readTimeout = ApiConstants.DEFAULT_READ_TIMEOUT_MILLIS;

	/**
	 * Write timeout.
	 *
	 * <p>
	 * Default value = {@value ApiConstants#DEFAULT_WRITE_TIMEOUT_MILLIS}ms.
	 */
	@Setter
	private long writeTimeout = ApiConstants.DEFAULT_WRITE_TIMEOUT_MILLIS;

	/**
	 * Channel token supplier of this client.
	 *
	 * <p>
	 * MUST BE NULL except you configured your own
	 */
	@Setter
	private ApiTokenSupplier apiTokenSupplier;

	/**
	 * Custom {@link Retrofit.Builder} used internally.
	 *
	 * <p>
	 * If you want to use your own setting, specify {@link Retrofit.Builder}
	 * instance. Default builder is used in case of {@code null} (default).
	 *
	 * <p>
	 * To use this method, please add dependency to
	 * 'com.squareup.retrofit2:retrofit'.
	 *
	 * @see #createDefaultRetrofitBuilder()
	 */
	@Setter
	private Retrofit.Builder retrofitBuilder;

	/**
	 * Add authentication header.
	 *
	 * <p>
	 * Default = {@code true}. If you manage authentication header yourself, set to
	 * {@code false}.
	 */
	@Setter
	private boolean addAuthenticationHeader = true;

	private OkHttpClient.Builder okHttpClientBuilder;

	/**
	 * Custom interceptors.
	 *
	 * <p>
	 * You can add your own interceptors.
	 *
	 * <p>
	 * Note: Authentication interceptor is automatically added by default.
	 *
	 * @see #addAuthenticationHeader(boolean)
	 */
	@Setter
	private List<Interceptor> additionalInterceptors = new ArrayList<>();

	/**
	 * Set fixed channel token. This overwrites
	 * {@link #channelTokenSupplier(ApiTokenSupplier)}.
	 *
	 * @see #channelTokenSupplier(ApiTokenSupplier)
	 */
	public ApiClientBuilder token(String token) {
		apiTokenSupplier(FixedApiTokenSupplier.of(token));
		return this;
	}

	/**
	 * Set customized OkHttpClient.Builder.
	 *
	 * <p>
	 * In case of you need your own customized {@link OkHttpClient}, this builder
	 * allows specify {@link OkHttpClient.Builder} instance.
	 *
	 * <p>
	 * To use this method, please add dependency to
	 * 'com.squareup.retrofit2:retrofit'.
	 *
	 * @param addAuthenticationHeader If it's true, the default authentication
	 *                                headers will be attached to all requests.
	 *                                Otherwise if it's false, you should insert
	 *                                your own authentication headers by yourself.
	 */
	public ApiClientBuilder okHttpClientBuilder(final @NonNull OkHttpClient.Builder okHttpClientBuilder,
			final boolean addAuthenticationHeader) {
		this.okHttpClientBuilder = okHttpClientBuilder;
		this.addAuthenticationHeader = addAuthenticationHeader;

		return this;
	}

	/**
	 * Creates a new {@link ApiService}.
	 */
	<T> T buildRetrofitIface(URI apiEndPoint, Class<T> retrofitIFace) {
		if (okHttpClientBuilder == null) {
			okHttpClientBuilder = new OkHttpClient.Builder();
		}

		// Add interceptors.
		if (addAuthenticationHeader) {
			okHttpClientBuilder.addInterceptor(buildAuthenticationInterceptor(apiTokenSupplier));
		}
		if (additionalInterceptors != null) {
			additionalInterceptors.forEach(okHttpClientBuilder::addInterceptor);
		}
		okHttpClientBuilder.addInterceptor(buildLoggingInterceptor());

		// Set timeout.
		okHttpClientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
				.readTimeout(readTimeout, TimeUnit.MILLISECONDS).writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);

		final OkHttpClient okHttpClient = okHttpClientBuilder.build();
		if (retrofitBuilder == null) {
			retrofitBuilder = createDefaultRetrofitBuilder();
		}
		
		retrofitBuilder.client(okHttpClient);
		retrofitBuilder.baseUrl(apiEndPoint.toString());

		final Retrofit retrofit = retrofitBuilder.build();

		return retrofit.create(retrofitIFace);
	}

	static HeaderInterceptor buildAuthenticationInterceptor(ApiTokenSupplier apiTokenSupplier) {
		requireNonNull(apiTokenSupplier, "apiTokenSupplier");
		return HeaderInterceptor.forApiTokenSupplier(apiTokenSupplier);
	}

	static Interceptor buildLoggingInterceptor() {
		final Logger slf4jLogger = LoggerFactory.getLogger("tw.com.chinalife.api.client.wire");

		return new HttpLoggingInterceptor(slf4jLogger::info).setLevel(Level.BODY);
	}

	static Retrofit.Builder createDefaultRetrofitBuilder() {
		return new Retrofit.Builder().addConverterFactory(JacksonConverterFactory.create(objectMapper));
	}

	/**
	 * Creates a new {@link ApiService}.
	 */
	public ApiClient build() {
		return new ApiClientImpl(buildRetrofitIface(apiEndPoint, ApiService.class));
	}

}
