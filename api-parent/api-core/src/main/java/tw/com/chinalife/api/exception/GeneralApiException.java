package tw.com.chinalife.api.exception;

import tw.com.chinalife.api.ApiCode;

public class GeneralApiException extends ApiException{
	private static final long serialVersionUID = 1L;


	public GeneralApiException(String message) {
		super(ApiCode.PROPERTY_VALIDATE_FAIL);
		this.addDetails(message);
	}
	
	
	
	public GeneralApiException(ApiCode code, String message) {
		super(code);
		this.addDetails(message);
	}
	
	public GeneralApiException(ApiCode code, String message,  final Throwable cause) {
		super(code, cause);
		this.addDetails(message);
	}
	
}
