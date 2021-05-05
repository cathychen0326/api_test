package tw.com.chinalife.api.client;

import static java.util.Collections.singletonMap;

import java.io.IOException;
import java.util.function.Function;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import retrofit2.Response;
import tw.com.chinalife.api.ApiCode;
import tw.com.chinalife.api.ApiConstants;
import tw.com.chinalife.api.client.model.ApiClientResponse;
import tw.com.chinalife.api.client.model.ApiErrorResponse;
import tw.com.chinalife.api.model.ErrorResponse;
import tw.com.chinalife.api.model.ErrorResult;

@Slf4j
class ExceptionConverter implements Function<Response<?>, ApiClientResponse> {
	public static final ObjectReader OBJECT_READER = new ObjectMapper().readerFor(ErrorResponse.class);

	@Override
	public ApiClientResponse apply(Response<?> response) {
		final String requestId = response.headers().get(ApiConstants.HEADER_REQUEST_ID);
		ApiErrorResponse error = new ApiErrorResponse();
		error.setRequestId(requestId);
		error.setHttpStatusCode(response.code());
		try {
			applyInternal(requestId, response, error);
		} catch (Exception e) {
			log.error("api error! requestId= {}", requestId, e);
			error.setApiCode(ApiCode.UNKNOW_ERROR);
		}

		return createResponse(error);

	}

	private ApiClientResponse<ErrorResult> createResponse(ApiErrorResponse error) {
		ApiClientResponse<ErrorResult> apiClientResponse = new ApiClientResponse<>(error.getRequestId(),
				error.getMessage(), error.getCode(), null);

		apiClientResponse.setErrorResponse(error);
		return apiClientResponse;
	}

	private static void applyInternal(final String requestId, final Response<?> response, ApiErrorResponse errorMessage)
			throws IOException {
		final ResponseBody responseBody = response.errorBody();
		System.out.println("============11111111111111==============");
		final ErrorResponse errorResponse = OBJECT_READER
				.with(new InjectableValues.Std(singletonMap("requestId", requestId)))
				.readValue(responseBody.byteStream());
		System.out.println("============2222222222222222222222222==============");

		errorMessage.setResponse(errorResponse);
	}
}
