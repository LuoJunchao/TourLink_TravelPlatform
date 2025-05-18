package org.tourlink.dataplatformservice.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class StringListJsonConverter extends JsonAttributeConverter<List<String>> {

    public StringListJsonConverter() {
        super(new TypeReference<>() {
        });
    }
}
