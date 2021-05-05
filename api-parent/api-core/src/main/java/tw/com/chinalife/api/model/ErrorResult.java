package tw.com.chinalife.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

@JsonTypeName(ErrorResult.ERROR_RESULT_TYPE)
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = ErrorResult.ErrorResultBuilder.class)
public class ErrorResult implements ReturnData {

	public static final String ERROR_RESULT_TYPE = "errorMessageDetail";
	List<String> details;

	private String type;

	@JsonPOJOBuilder(withPrefix = "")
	public static class ErrorResultBuilder {
	}

}
