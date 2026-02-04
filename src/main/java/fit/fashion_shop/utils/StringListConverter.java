/*
 * @ (#) StringListConverter.java    1.0    14/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.utils;/*
 * @description:
 * @author: Bao Thong
 * @date: 14/01/2026
 * @version: 1.0
 */

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }
        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi chuyển đổi danh sách ảnh sang JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return mapper.readValue(dbData, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi chuyển đổi JSON sang danh sách ảnh", e);
        }
    }
}
