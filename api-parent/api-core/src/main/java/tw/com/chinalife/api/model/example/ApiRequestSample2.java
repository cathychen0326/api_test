package tw.com.chinalife.api.model.example;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import tw.com.chinalife.api.model.ApiRequest;

@JsonTypeName("bat")
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = ApiRequestSample2.ApiRequestSample2Builder.class)
public class ApiRequestSample2 implements ApiRequest {
	private String apiId = "bat";
	private String message;

	@JsonPOJOBuilder(withPrefix = "")
	public static class ApiRequestSample2Builder {
		// Providing builder instead of public constructor. Class body is filled by
		// lombok.
	}
}
