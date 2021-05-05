package tw.com.chinalife.api.model.example;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import tw.com.chinalife.api.model.ReturnData;

@JsonTypeName("bat")
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = ReturnObjectSample2.ReturnObjectSample2Builder.class)
public class ReturnObjectSample2 implements ReturnData {
	private String sample = "bat";
	private String myMessage;

	@JsonPOJOBuilder(withPrefix = "")
	public static class ReturnObjectSample2Builder {
		// Providing builder instead of public constructor. Class body is filled by
		// lombok.
	}

	public String getType() {
		return "bat";
	}
}
