package tw.com.chinalife.api.model.example;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import tw.com.chinalife.api.model.ApiRequest;

@JsonTypeName("test")
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = ApiRequestSample.ApiRequestSampleBuilder.class)
public class ApiRequestSample implements ApiRequest {
	private String apiId = "test";
	
    @NotBlank(message = "message 不能為空!")
    @Size(min = 2)
	private String message;

	@JsonPOJOBuilder(withPrefix = "")
	public static class ApiRequestSampleBuilder {
		// Providing builder instead of public constructor. Class body is filled by
		// lombok.
	}
}
