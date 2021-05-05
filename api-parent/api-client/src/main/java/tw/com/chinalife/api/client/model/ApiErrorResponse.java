package tw.com.chinalife.api.client.model;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import tw.com.chinalife.api.ApiCode;
import tw.com.chinalife.api.model.ApiMessage;
import tw.com.chinalife.api.model.ErrorResponse;
import tw.com.chinalife.api.model.ErrorResult;

@Data
public class ApiErrorResponse implements ApiMessage {

	public void setApiCode(ApiCode apiCode) {
		this.code = apiCode.getCode();
		this.message = apiCode.getMessage();
	}

	public void setResponse(ErrorResponse response) {
		BeanUtils.copyProperties(response, this);
	}

	private String requestId;
	private int httpStatusCode;
	private String code;
	private String message;
	ErrorResult result;

}
