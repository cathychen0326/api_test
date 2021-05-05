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

import lombok.Data;
import tw.com.chinalife.api.ApiCode;

/**
 * Generic response object for LINE Messaging API.
 */
@Data
public class ApiResponse<E extends ReturnData> implements ApiMessage {

	public ApiResponse(ApiCode apiCode, E result) {
		this.message = apiCode.getMessage();
		this.code = apiCode.getCode();
		this.result = result;
	}

	public ApiResponse(String requestId, String message, String code, E result) {
		this.requestId = requestId;
		this.message = message;
		this.code = code;
		this.result = result;
	}

	/**
	 * Value from {@literal X-API-Request-Id} header.
	 */
	String requestId;
	String code;
	String message;
	E result;

}
