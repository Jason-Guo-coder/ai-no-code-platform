package com.gjq.ainocodeplatform.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JsonConfigTest {

    private static final long UNSAFE_JAVASCRIPT_INTEGER = 9007199254740993L;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 验证包装类型和基本类型的 Long 均以字符串形式输出。
     */
    @Test
    void shouldSerializeLongValuesAsStrings() {
        JsonNode jsonNode = objectMapper.valueToTree(new LongPayload(
                UNSAFE_JAVASCRIPT_INTEGER, UNSAFE_JAVASCRIPT_INTEGER));

        assertTrue(jsonNode.get("boxedId").isTextual());
        assertTrue(jsonNode.get("primitiveId").isTextual());
        assertEquals("9007199254740993", jsonNode.get("boxedId").asText());
        assertEquals("9007199254740993", jsonNode.get("primitiveId").asText());
    }

    private record LongPayload(Long boxedId, long primitiveId) {
    }
}
