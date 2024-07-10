package com.xkcoding.cache.redis.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        OBJECT_MAPPER = objectMapper;
    }

    private JsonUtils() {
        // this is a util
    }

    public static <T> String toJson(T object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("serialize to json fail, please check the input param, object:{}", object, e);
            throw new IllegalArgumentException("serialize to json fail, please check the input param", e);
        }
    }

    public static <T> T toObject(String content, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(content, clazz);
        } catch (JsonProcessingException e) {
            log.error("deserialize to object fail, please check the input param, content:{}, clazz:{}",
                      content,
                      clazz,
                      e);
            throw new IllegalArgumentException("deserialize to object fail, please check the input String param", e);
        } catch (IOException e) {
            log.error("deserialize to object fail, content:{}, clazz:{}", content, clazz, e);
            throw e;
        }
    }

    public static <T> T toObject(String content, TypeReference<T> valueTypeRef) {
        try {
            return OBJECT_MAPPER.readValue(content, valueTypeRef);
        } catch (JsonProcessingException e) {
            log.error("deserialize to object fail, please check the input param, content:{}, valueTypeRef:{}",
                      content,
                      valueTypeRef,
                      e);
            throw new IllegalArgumentException("deserialize to object fail, please check the input String param", e);
        } catch (IOException e) {
            log.error("deserialize to object fail, content:{}, valueTypeRef:{}", content, valueTypeRef, e);
            throw e;
        }
    }

    public static <T> T toObject(JsonNode node, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.treeToValue(node, valueType);
        } catch (JsonProcessingException e) {
            log.error("deserialize to object fail, please check the input param, node:{}, valueTypeRef:{}",
                      node,
                      valueType,
                      e);
            throw new IllegalArgumentException("deserialize to object fail, please check the input JsonNode param", e);
        }
    }

    public static <T> T toObject(JsonNode node, TypeReference<T> valueTypeRef) {
        return toObject(node.toString(), valueTypeRef);
    }

    public static JsonNode parseObject2Node(Object o) {
        return OBJECT_MAPPER.valueToTree(o);
    }

    public static JsonNode parseObject(String content) {
        try {
            return OBJECT_MAPPER.readTree(content);
        } catch (JsonProcessingException e) {
            log.error("deserialize to JsonNode fail, please check the input param, content:{}", content, e);
            throw new IllegalArgumentException("deserialize to JsonNode fail, please check the input param", e);
        } catch (IOException e) {
            log.error("deserialize to JsonNode fail! content:{}", content, e);
            throw e;
        }
    }

}
