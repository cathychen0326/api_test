package tw.com.chinalife.api.exception;

import tw.com.chinalife.api.ApiCode;

public class MethodInvokeException  extends GeneralApiException{
	private static final long serialVersionUID = 1L;

	public MethodInvokeException(String message) {
		super(ApiCode.METHOD_INVOKE_ERROR, message);
	}

	public MethodInvokeException(String message, Exception e) {
		super(ApiCode.METHOD_INVOKE_ERROR, message ,e);
		Throwable t = e.getCause();
		if(t instanceof ApiException) {
			ApiException exception = (ApiException)t;
			this.setCode(exception.getCode());
			this.setMessage(exception.getMessage());
		}
	}
	
}
