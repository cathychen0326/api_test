package tw.com.chinalife.api.model.example;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import tw.com.chinalife.api.model.ReturnData;

@JsonTypeName("test")
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = ReturnObjectSample.ReturnObjectSampleBuilder.class)
public class ReturnObjectSample implements ReturnData {
	private String sample = "sample";
	private String myMessage;

	@JsonPOJOBuilder(withPrefix = "")
	public static class ReturnObjectSampleBuilder {
		// Providing builder instead of public constructor. Class body is filled by
		// lombok.
	}

	public String getType() {
		return "test";
	}
}
