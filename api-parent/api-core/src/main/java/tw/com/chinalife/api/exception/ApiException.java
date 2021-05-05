package tw.com.chinalife.api.exception;

import java.util.ArrayList;
import java.util.List;

import tw.com.chinalife.api.ApiCode;
public class ApiException extends RuntimeException{
	protected static final long SERIAL_VERSION_UID = 1l;
	private static final long serialVersionUID = SERIAL_VERSION_UID;
	private String code ;

	private String message ;
	private List<String	> details;
	
	public ApiException(final ApiCode apiCode){
		super( formatMessage(apiCode));
		this.code = apiCode.getCode();
		this.message = apiCode.getMessage();
	}
	
	public ApiException(final ApiCode apiCode , final Throwable cause){
		super( formatMessage(apiCode) , cause);
		this.code = apiCode.getCode();
		this.message = apiCode.getMessage();
	}
	
	public ApiException(final ApiCode apiCode , List<String> details, final Throwable cause){
		super( formatMessage(apiCode) , cause);
		this.code = apiCode.getCode();
		this.message = apiCode.getMessage();
		this.details = details;
	}
	
	private static String formatMessage(ApiCode apiCode) {
		return "error code: " + apiCode.getCode() + ", error message: " + apiCode.getMessage();
	}

	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getDetails() {
		return details;
	}
	
	public void addDetails(String detail) {
		if(details==null) {
			details = new ArrayList<>();
		}
		details.add(detail);
	}

}
