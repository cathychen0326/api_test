
package tw.com.chinalife.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = UnknownRequest.UnknownEventBuilder.class)
public class UnknownRequest implements ApiRequest {
	@JsonPOJOBuilder(withPrefix = "")
	public static class UnknownEventBuilder {
	}

	String apiId;

}
