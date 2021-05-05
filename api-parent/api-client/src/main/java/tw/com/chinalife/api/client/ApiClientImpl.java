package tw.com.chinalife.api.client;

import java.util.concurrent.CompletableFuture;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tw.com.chinalife.api.ApiCode;
import tw.com.chinalife.api.ApiConstants;
import tw.com.chinalife.api.client.model.ApiClientResponse;
import tw.com.chinalife.api.exception.ApiException;
import tw.com.chinalife.api.model.ApiRequest;
import tw.com.chinalife.api.model.ReturnData;

/**
 * Proxy implementation of {@link ApiClient} to hind internal implementation.
 */
@Slf4j
@AllArgsConstructor
public class ApiClientImpl<E extends ApiRequest, T extends ReturnData> implements ApiClient<E, T> {
	static final ExceptionConverter EXCEPTION_CONVERTER = new ExceptionConverter();

	private final ApiService retrofitImpl;

	@Override
	public CompletableFuture<ApiClientResponse<T>> getMessage(E request) {
		return toApiResponseFuture(retrofitImpl.getSample(request));
	}

	private CompletableFuture<ApiClientResponse<T>> toApiResponseFuture(final Call<ApiResponseBody> callToWrap) {
		final ApiCallbackAdaptor completableFuture = new ApiCallbackAdaptor();
		callToWrap.enqueue(completableFuture);
		return completableFuture;
	}

//	static class CallbackAdaptor<T> extends CompletableFuture<T> implements Callback<T> {
//		@Override
//		public void onResponse(final Call<T> call, final Response<T> response) {
//			if (response.isSuccessful()) {
//				complete(response.body());
//			} else {
//				
//				completeExceptionally(EXCEPTION_CONVERTER.apply(response));
//			}
//		}
//
//		@Override
//		public void onFailure(final Call<T> call, final Throwable t) {
//			completeExceptionally(new GeneralApiException(t.getMessage(), null, t));
//		}
//	}

	class ApiCallbackAdaptor extends CompletableFuture<ApiClientResponse<T>> implements Callback<ApiResponseBody> {
		@Override
		public void onResponse(final Call<ApiResponseBody> call, final Response<ApiResponseBody> response) {
			log.debug("=================response========={} ,  {}", response, response.isSuccessful());
			log.debug("=================call========={}", call);
			if (response.isSuccessful()) {
				final String requestId = response.headers().get(ApiConstants.HEADER_REQUEST_ID);
				complete(response.body().withRequestId(requestId));
			} else {
				complete(EXCEPTION_CONVERTER.apply(response));
			}
		}

		@Override
		public void onFailure(final Call<ApiResponseBody> call, final Throwable t) {
			completeExceptionally(new ApiException(ApiCode.API_CLIENT_ERROR, t));
		}
	}

}
