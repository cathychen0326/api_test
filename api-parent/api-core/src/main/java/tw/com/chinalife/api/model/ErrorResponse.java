/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package tw.com.chinalife.api.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/**
 * Error response from LINE Messaging Server.
 *
 * @see <a href=
 *      "https://developers.line.me/en/reference/messaging-api/#error-responses">//developers.line.me/en/reference/messaging-api/#error-responses</a>
 */
@Value
public class ErrorResponse implements ApiMessage {

	/** Request ID in response header. */
	String requestId;

	private String type;

	private String code;

	/** Summary or details of the error. */
	String message;

	/**
	 * Details of the error.
	 *
	 * <p>
	 * Always non-null but can be empty.
	 */
	ErrorResult result;

	public ErrorResponse(@JacksonInject("requestId") final String requestId, @JsonProperty("code") final String code,
			@JsonProperty("message") final String message, @JsonProperty("details") final List<String> details,
			@JsonProperty(value = "type", required = false) String type) {
		this.requestId = requestId;
		this.code = code;
		this.message = message;
		this.result = createResult(details);
		this.type = type;
	}

	private ErrorResult createResult(final List<String> details) {
		String errorType = ErrorResult.ERROR_RESULT_TYPE;
		return details != null ? new ErrorResult(details, errorType) : new ErrorResult(Collections.emptyList(), errorType);
	}

}
