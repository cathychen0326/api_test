
package tw.com.chinalife.api.client;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import tw.com.chinalife.api.ApiConstants;
import tw.com.chinalife.api.model.ApiRequest;

interface ApiService {
	/**
	 * Method for Retrofit.
	 *
	 * @see ApiClient#getSample(ApiRequest)
	 */
	@POST(ApiConstants.API_HANDLER_PATH)
	Call<ApiResponseBody> getSample(@Body ApiRequest request);

}
