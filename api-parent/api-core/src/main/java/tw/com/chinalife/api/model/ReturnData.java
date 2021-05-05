package tw.com.chinalife.api.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import tw.com.chinalife.api.model.example.ReturnObjectSample;
import tw.com.chinalife.api.model.example.ReturnObjectSample2;

@JsonSubTypes({ @JsonSubTypes.Type(ErrorResult.class), @JsonSubTypes.Type(ReturnObjectSample.class), @JsonSubTypes.Type(ReturnObjectSample2.class) })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
public interface ReturnData {
}
