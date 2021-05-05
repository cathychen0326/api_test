package tw.com.chinalife.api.client.model;

import lombok.Data;
import tw.com.chinalife.api.ApiCode;
import tw.com.chinalife.api.model.ApiResponse;
import tw.com.chinalife.api.model.ReturnData;

@Data
public class ApiClientResponse<E extends ReturnData> extends ApiResponse<E> {

	private boolean success = false;

	public ApiClientResponse(ApiCode apiCode, E result) {
		super(apiCode, result);
		this.success = true;
	}

	public ApiClientResponse(String requestId, String message, String code, E result) {
		super(requestId, message, code, result);
		this.success = true;
	}

	ApiErrorResponse errorResponse;

	public void setErrorResponse(ApiErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
		this.success = false;
	}

}
