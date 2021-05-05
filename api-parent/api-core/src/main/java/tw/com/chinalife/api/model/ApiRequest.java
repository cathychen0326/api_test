package tw.com.chinalife.api.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import tw.com.chinalife.api.model.example.ApiRequestSample;
import tw.com.chinalife.api.model.example.ApiRequestSample2;

@JsonSubTypes({ @JsonSubTypes.Type(ApiRequestSample.class), @JsonSubTypes.Type(ApiRequestSample2.class) })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = UnknownRequest.class, visible = true)
public interface ApiRequest {
	String getApiId();
}
