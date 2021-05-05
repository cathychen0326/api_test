package tw.com.chinalife.api.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import tw.com.chinalife.api.ApiCode;
import tw.com.chinalife.api.ApiConstants;
import tw.com.chinalife.api.annotation.ApiMapping;
import tw.com.chinalife.api.annotation.ApiMessageHandler;
import tw.com.chinalife.api.annotation.ApiMessages;
import tw.com.chinalife.api.exception.ApiException;
import tw.com.chinalife.api.exception.ApiValidationException;
import tw.com.chinalife.api.exception.MethodInvokeException;
import tw.com.chinalife.api.exception.UnsupportApiException;
import tw.com.chinalife.api.model.ApiRequest;
import tw.com.chinalife.api.model.ApiResponse;
import tw.com.chinalife.api.model.ErrorResponse;

@Slf4j
@RestController
@ConditionalOnProperty(name = "solar.api.handler.enabled", havingValue = "true", matchIfMissing = true)
public class ApiHandlerSupport {
	private static final Comparator<HandlerMethod> HANDLER_METHOD_PRIORITY_COMPARATOR = Comparator
			.comparing(HandlerMethod::getPriority).reversed();
	private final ConfigurableApplicationContext applicationContext;

	volatile List<HandlerMethod> eventConsumerList;

	private Validator validator;

	@Autowired
	public ApiHandlerSupport(final ConfigurableApplicationContext applicationContext, Validator validator) {
		this.applicationContext = applicationContext;
		this.validator = validator;
		applicationContext.addApplicationListener(event -> {
			if (event instanceof ContextRefreshedEvent) {
				refresh();
			}
		});
	}

	@VisibleForTesting
	void refresh() {
		final Map<String, Object> handlerBeanMap = applicationContext.getBeansWithAnnotation(ApiMessageHandler.class);

		final List<HandlerMethod> collect = handlerBeanMap.values().stream().flatMap((Object bean) -> {
			final Method[] uniqueDeclaredMethods = ReflectionUtils.getUniqueDeclaredMethods(bean.getClass());

			return Arrays.stream(uniqueDeclaredMethods).map(method -> getMethodHandlerMethodFunction(bean, method))
					.filter(Objects::nonNull);
		}).sorted(HANDLER_METHOD_PRIORITY_COMPARATOR).collect(Collectors.toList());

		log.info("Registered API handler: count = {}", collect.size());
		collect.forEach(
				item -> log.info("Mapped \"{}\" onto {}", item.getSupportType(), item.getHandler().toGenericString()));

		eventConsumerList = collect;
	}

	private HandlerMethod getMethodHandlerMethodFunction(Object consumer, Method method) {
		final ApiMapping mapping = AnnotatedElementUtils.getMergedAnnotation(method, ApiMapping.class);
		if (mapping == null) {
			return null;
		}

		Preconditions.checkState(method.getParameterCount() == 1, "Number of parameter should be 1. But {}",
				(Object[]) method.getParameterTypes());
		// TODO: Support more than 1 argument. Like MVC's argument resolver?

		final Type type = method.getGenericParameterTypes()[0];

		final Predicate<ApiRequest> predicate = new EventPredicate(type);
		return new HandlerMethod(predicate, consumer, method, getPriority(mapping, type));
	}

	private int getPriority(final ApiMapping mapping, final Type type) {
		if (mapping.priority() != ApiMapping.DEFAULT_PRIORITY_VALUE) {
			return mapping.priority();
		}

		if (type == ApiRequest.class) {
			return ApiMapping.DEFAULT_PRIORITY_FOR_EVENT_IFACE;
		}

		if (type instanceof Class) {
			return ((Class<?>) type).isInterface() ? ApiMapping.DEFAULT_PRIORITY_FOR_IFACE
					: ApiMapping.DEFAULT_PRIORITY_FOR_CLASS;
		}

		if (type instanceof ParameterizedType) {
			return ApiMapping.DEFAULT_PRIORITY_FOR_PARAMETRIZED_TYPE;
		}

		throw new IllegalStateException();
	}

	@Value
	static class HandlerMethod {
		Predicate<ApiRequest> supportType;
		Object object;
		Method handler;
		int priority;
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		log.error(ex.getMessage(), ex);
		if (ex instanceof ApiValidationException) {
			ApiValidationException serverError = (ApiValidationException) ex;
			return createApiErrorResponse(serverError, HttpStatus.BAD_REQUEST);

		} else if (ex instanceof ApiException) {
			ApiException serverError = (ApiException) ex;
			return createApiErrorResponse(serverError, HttpStatus.INTERNAL_SERVER_ERROR);

		} else {
			return createErrorResponse(ApiCode.API_SERVER_ERROR);

		}
	}

	@PostMapping(ApiConstants.API_HANDLER_PATH)
	public ApiResponse callback(@Validated @ApiMessages ApiRequest request)
			throws MethodInvokeException, ApiValidationException {
		return dispatch(request);
	}

	@VisibleForTesting
	ApiResponse dispatch(ApiRequest request) throws MethodInvokeException, ApiValidationException {
		try {
			return dispatchInternal(request);
		} catch (InvocationTargetException e) {
			log.error("InvocationTargetException occurred.", e);
			throw new MethodInvokeException("Api error: method invoke error", e);
		} catch (ApiValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new MethodInvokeException(e.getMessage(), e);
		}
	}

	private ApiResponse dispatchInternal(final ApiRequest request)
			throws UnsupportApiException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final HandlerMethod handlerMethod = eventConsumerList.stream()
				.filter(consumer -> consumer.getSupportType().test(request)).findFirst()
				.orElseThrow(() -> new UnsupportApiException("Unsupported Api . apiId:" + request.getApiId()));

		validParameters(request);

		final Object returnValue = handlerMethod.getHandler().invoke(handlerMethod.getObject(), request);

		return (ApiResponse) returnValue;
	}

	private void validParameters(final ApiRequest request) {
		Set<ConstraintViolation<ApiRequest>> violationSet = validator.validate(request);
		boolean hasError = violationSet != null && violationSet.size() > 0;
		if (hasError) {
			ApiValidationException ex = new ApiValidationException();
			for (ConstraintViolation<ApiRequest> violation : violationSet) {
				ex.addDetails(getValidMessage(violation));
			}
			throw ex;
		}
	}

	private String getValidMessage(ConstraintViolation<ApiRequest> violation) {
		return String.format("欄位『%s』錯誤: %s", violation.getPropertyPath().toString(), violation.getMessage());
	}

	private ResponseEntity<ErrorResponse> createApiErrorResponse(ApiException serverError, HttpStatus status) {
		ErrorResponse resp = new ErrorResponse(null, serverError.getCode(), serverError.getMessage(),
				serverError.getDetails());

		return ResponseEntity.status(status).body(resp);
	}

	private ResponseEntity<ErrorResponse> createErrorResponse(ApiCode serverError) {
		return createErrorResponse(serverError, null);
	}

	private ResponseEntity<ErrorResponse> createErrorResponse(ApiCode serverError, List<String> details) {
		ErrorResponse resp = new ErrorResponse(null, serverError.getCode(), serverError.getMessage(), details);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
	}
}
