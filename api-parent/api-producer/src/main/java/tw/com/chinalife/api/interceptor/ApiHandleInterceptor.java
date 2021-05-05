package tw.com.chinalife.api.interceptor;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.com.chinalife.api.ApiCode;
import tw.com.chinalife.api.ApiConstants;
import tw.com.chinalife.api.annotation.ApiMessages;
import tw.com.chinalife.api.model.ApiRequest;
import tw.com.chinalife.api.model.ErrorResponse;
import tw.com.chinalife.api.objectmapper.ModelObjectMapper;
import tw.com.chinalife.api.parser.WebhookParseException;
import tw.com.chinalife.api.parser.WebhookParser;
import tw.com.chinalife.api.support.ApiHandlerArgumentProcessor;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ApiHandleInterceptor implements HandlerInterceptor {
	private final WebhookParser webhookParser;
	private final ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		final HandlerMethod hm = (HandlerMethod) handler;
		final MethodParameter[] methodParameters = hm.getMethodParameters();

		for (MethodParameter methodParameter : methodParameters) {
			if (methodParameter.getParameterAnnotation(ApiMessages.class) == null) {
				continue;
			}
			try {
				final String signatureHeader = request.getHeader(ApiConstants.SIGNATURE_HEADER_NAME);
				final byte[] payload = StreamUtils.copyToByteArray(request.getInputStream());
				final ApiRequest callbackRequest = webhookParser.handle(signatureHeader, payload);
				ApiHandlerArgumentProcessor.setValue(request, callbackRequest);
				return true;
			} catch (WebhookParseException e) {
				log.info("Api parse exception: {}", e.getMessage());
				response.sendError(HttpStatus.BAD_REQUEST.value());
			    List<String> details = createErrorDetail(e);
				ErrorResponse errorResponse = createErrorResponse(ApiCode.SIGNATURE_HEADER_ERROR, details);
				try (PrintWriter writer = response.getWriter()) {
					writer.println(objectMapper.writeValueAsString(errorResponse) );
				}
				return false;
			}
		}
		return true;
	}




	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

//	private ErrorResponse createErrorResponse(ApiCode serverError) {
//		return createErrorResponse(serverError ,null );
//	}
//	
	private ErrorResponse createErrorResponse(ApiCode serverError, List<String> details) {
		return new ErrorResponse(null, serverError.getCode(), serverError.getMessage(),details );
	}
	private List<String> createErrorDetail(WebhookParseException e) {
		List<String> details = new ArrayList<>();
		details.add(e.getMessage());
		return details;
	}

}
