package tw.com.chinalife.api;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "solar.api")
public class ApiProperties {

	private @Valid @NotNull String token;

	private @Valid @NotNull String secretKey;

	private URI endPoint;
	/**
	 * Connection timeout in milliseconds.
	 */
	private @Valid @NotNull long connectTimeout = ApiConstants.DEFAULT_CONNECT_TIMEOUT_MILLIS;

	/**
	 * Read timeout in milliseconds.
	 */
	private @Valid @NotNull long readTimeout = ApiConstants.DEFAULT_READ_TIMEOUT_MILLIS;

	/**
	 * Write timeout in milliseconds.
	 */
	private @Valid @NotNull long writeTimeout = ApiConstants.DEFAULT_WRITE_TIMEOUT_MILLIS;

}
