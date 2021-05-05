package tw.com.chinalife.api.exception;

import tw.com.chinalife.api.ApiCode;

public class ApiValidationException extends ApiException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ApiValidationException() {
		super(ApiCode.PROPERTY_VALIDATE_FAIL);
	}
	
}
