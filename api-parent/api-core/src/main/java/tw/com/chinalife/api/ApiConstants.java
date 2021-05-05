
package tw.com.chinalife.api;

import java.net.URI;

/**
 * Common constant holder.
 */
public enum ApiConstants {
	/* Only public static final fields in this enum. */;
	public static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 10_000;
	public static final long DEFAULT_READ_TIMEOUT_MILLIS = 10_000;
	public static final long DEFAULT_WRITE_TIMEOUT_MILLIS = 10_000;

	public static final URI DEFAULT_API_END_POINT = URI.create("http://localhost:8080/");

	public static final String API_HANDLER_PATH = "api/handler";
	public static final String SIGNATURE_HEADER_NAME = "x-api-signature";
	public static final String HEADER_REQUEST_ID = "x-api-request-id";
	

}
