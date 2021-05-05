package tw.com.chinalife.api.support;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Predicate;

import com.google.common.base.Preconditions;

import tw.com.chinalife.api.model.ApiRequest;

public class EventPredicate implements Predicate<ApiRequest>{
	private final Class<?> supportRequest;

	@SuppressWarnings("unchecked")
	EventPredicate(final Type mapping) {
		if (mapping instanceof Class) {
			Preconditions.checkState(ApiRequest.class.isAssignableFrom((Class<?>) mapping),
					"Handler argument type should BE-A Event. But {}", mapping.getClass());
			supportRequest = (Class<? extends ApiRequest>) mapping;
		} else {
			final ParameterizedType parameterizedType = (ParameterizedType) mapping;
			supportRequest = (Class<? extends ApiRequest>) parameterizedType.getRawType();
		}
	}

	@Override
	public boolean test(final ApiRequest request) {
		return supportRequest.isAssignableFrom(request.getClass());
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append('[');
		sb.append(supportRequest.getSimpleName());
		sb.append(']');

		return sb.toString();
	}
}
