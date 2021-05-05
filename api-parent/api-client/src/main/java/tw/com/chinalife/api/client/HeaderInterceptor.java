package tw.com.chinalife.api.client;

import java.io.IOException;

import lombok.AllArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@AllArgsConstructor(staticName = "forApiTokenSupplier")
class HeaderInterceptor implements Interceptor {
	// TODO change it!!
	private static final String USER_AGENT = "api-client/"
			+ HeaderInterceptor.class.getPackage().getImplementationVersion();
	private final ApiTokenSupplier channelTokenSupplier;

	@Override
	public Response intercept(Chain chain) throws IOException {
		final String channelToken = channelTokenSupplier.get();
		Request request = chain.request().newBuilder()
				.addHeader("Authorization", "Bearer " + channelToken)
				.addHeader("User-Agent", USER_AGENT)
				.addHeader("Content-Type", "application/json; charset=utf-8")
				.build();
		return chain.proceed(request);
	}

}
