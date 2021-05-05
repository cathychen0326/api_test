package tw.com.chinalife.api.client;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

import tw.com.chinalife.api.ApiConstants;
import tw.com.chinalife.api.client.model.ApiClientResponse;
import tw.com.chinalife.api.model.ApiRequest;
import tw.com.chinalife.api.model.ReturnData;

public interface ApiClient<E extends ApiRequest, T extends ReturnData> {

	CompletableFuture<ApiClientResponse<T>> getMessage(E request);

	URI apiEndPoint = ApiConstants.DEFAULT_API_END_POINT;

	static ApiClientBuilder builder(String channelToken) {
		return builder(FixedApiTokenSupplier.of(channelToken));
	}

	static ApiClientBuilder builder(ApiTokenSupplier apiTokenSupplier) {
		return new ApiClientBuilder().apiTokenSupplier(apiTokenSupplier);
	}
}
