package org.tourlink.dataplatformservice.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;

import java.util.Map;

@Converter
public class StringDoubleMapJsonConverter extends JsonAttributeConverter<Map<String, Double>> {

    public StringDoubleMapJsonConverter() {
        super(new TypeReference<>() {
        });
    }
}
