package tw.com.chinalife.api.exception;

import tw.com.chinalife.api.ApiCode;

public class UnsupportApiException extends GeneralApiException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportApiException(String message) {
		super(ApiCode.UNSUPPORTED_API, message);
	}
	

}
