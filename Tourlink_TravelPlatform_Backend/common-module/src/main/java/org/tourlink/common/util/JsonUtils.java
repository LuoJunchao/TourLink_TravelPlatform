package org.tourlink.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 */
@Slf4j
public class JsonUtils {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static {
        // 注册Java 8日期时间模块
        objectMapper.registerModule(new JavaTimeModule());
        // 禁用将日期时间序列化为时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    /**
     * 将对象转换为JSON字符串
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("转换对象到JSON字符串失败", e);
            throw new RuntimeException("转换对象到JSON字符串失败", e);
        }
    }
    
    /**
     * 将JSON字符串转换为对象
     * @param json JSON字符串
     * @param clazz 目标类型
     * @param <T> 目标类型
     * @return 对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("转换JSON字符串到对象失败", e);
            throw new RuntimeException("转换JSON字符串到对象失败", e);
        }
    }
    
    /**
     * 将JSON字符串转换为复杂类型对象
     * @param json JSON字符串
     * @param typeReference 类型引用
     * @param <T> 目标类型
     * @return 对象
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            log.error("转换JSON字符串到复杂类型对象失败", e);
            throw new RuntimeException("转换JSON字符串到复杂类型对象失败", e);
        }
    }
    
    /**
     * 将JSON字符串转换为Map
     * @param json JSON字符串
     * @return Map对象
     */
    public static Map<String, Object> jsonToMap(String json) {
        return fromJson(json, new TypeReference<Map<String, Object>>() {});
    }
    
    /**
     * 将JSON字符串转换为List
     * @param json JSON字符串
     * @param clazz 列表元素类型
     * @param <T> 列表元素类型
     * @return List对象
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            log.error("转换JSON字符串到List失败", e);
            throw new RuntimeException("转换JSON字符串到List失败", e);
        }
    }
    
    /**
     * 获取ObjectMapper实例
     * @return ObjectMapper实例
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
