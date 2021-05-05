package tw.com.chinalife.api;

public enum ApiCode {
	SUCESS					("XS0000", "成功"),
	NOT_FOUND				("XF0001", "找不到對應的API HANDLER"),
	UNSUPPORTED_API			("XF0002", "找不到對應的API"),
	METHOD_INVOKE_ERROR		("XF0003", "API_SERVER發生不可預期錯誤"),
	PROPERTY_VALIDATE_FAIL  ("XF0004", "API_PROPERTIES 欄位資料錯誤"),
	SIGNATURE_HEADER_ERROR	("XF0010", "Http Header signature error!!"),
	API_SERVER_ERROR		("XF0500", "API_SERVER發生錯誤錯誤"),
	API_CLIENT_ERROR		("XF0600", "API_CLIENT不明錯誤"),
	
	UNKNOW_ERROR			("XF9999", "不明錯誤");
	
	private String code;
	private String message;

	private ApiCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
