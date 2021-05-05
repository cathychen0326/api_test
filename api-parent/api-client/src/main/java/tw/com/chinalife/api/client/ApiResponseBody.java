
package tw.com.chinalife.api.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import tw.com.chinalife.api.client.ApiResponseBody.ApiResponseBodyBuilder;
import tw.com.chinalife.api.client.model.ApiClientResponse;
import tw.com.chinalife.api.model.ReturnData;

@Value
@Builder
@JsonDeserialize(builder = ApiResponseBodyBuilder.class)
class ApiResponseBody {
	@JsonPOJOBuilder(withPrefix = "")
	public static class ApiResponseBodyBuilder {
		// filled by lombok.
	}

	String code;
	String message;
	ReturnData result;

	ApiClientResponse withRequestId(final String requestId) {
		return new ApiClientResponse(requestId, message, code, result);
	}
}
