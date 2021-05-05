package tw.com.chinalife.server;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import tw.com.chinalife.api.ApiCode;
import tw.com.chinalife.api.annotation.ApiMapping;
import tw.com.chinalife.api.annotation.ApiMessageHandler;
import tw.com.chinalife.api.exception.GeneralApiException;
import tw.com.chinalife.api.model.ApiResponse;
import tw.com.chinalife.api.model.example.ApiRequestSample;
import tw.com.chinalife.api.model.example.ApiRequestSample2;
import tw.com.chinalife.api.model.example.ReturnObjectSample;
import tw.com.chinalife.api.model.example.ReturnObjectSample2;

@ApiMessageHandler
@Controller
@Slf4j
public class MyController {
	@ApiMapping
	public ApiResponse handleDefault(@Valid ApiRequestSample request)  {
		log.debug("apiId: {}", request.getApiId());
		log.debug("message: {}", request.getMessage());

		if(request.getMessage().equals("success") || !StringUtils.hasText(request.getMessage())) {
			ReturnObjectSample data = ReturnObjectSample.builder().myMessage("這是成功的測試").build();
			return new ApiResponse(ApiCode.SUCESS, data);
		}
		else if(request.getMessage().equals("runtime")) {
			throw new RuntimeException("23222");	
		}
		throw new GeneralApiException(ApiCode.API_SERVER_ERROR,"123");
	}

	@ApiMapping
	public ApiResponse handleDefault2(ApiRequestSample2 request) {
		System.out.println("apiId: " + request.getApiId());
		System.out.println("message: " + request.getMessage());

		ReturnObjectSample2 data = ReturnObjectSample2.builder().myMessage("bat ApiRequestSample2 test ").build();
		
		return new ApiResponse(ApiCode.SUCESS, data);

	}
}
