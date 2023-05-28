package titan.lightbatis.mybatis.handler;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.MappedTypes;

import java.util.Objects;

@Slf4j
@MappedTypes(JSONObject.class)
public class JacksonTypeHandler extends AbstractJsonTypeHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 未知字段忽略
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 不使用科学计数
        MAPPER.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        // null 值不输出(节省内存)
        MAPPER.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
    }

    public JacksonTypeHandler() {
        super(JSONObject.class);
    }
    public JacksonTypeHandler(Class<?> classType) {
        super(classType);
    }

    @Override
    protected Object parse(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        try {
            return MAPPER.readValue(jsonStr, classType);
        } catch (JsonProcessingException e) {
            log.error("parse failed, jsonStr={}", jsonStr, e);
            return null;
        }
    }

    @Override
    protected String toJson(Object obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Object to json string failed!" + obj, e);
        }
    }
}