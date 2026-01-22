/*
 * @ (#) CustomizationConfigResponse.java    1.0    22/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 22/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.ProductCustomizationConfig;
import fit.fashion_shop.enums.StepType;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
public record CustomizationConfigResponse(
        Long id,
        StepType stepType,
        boolean enabled,
        Double extraPrice,
        Object configData
) {
    public static CustomizationConfigResponse fromEntity(ProductCustomizationConfig entity, ObjectMapper mapper) {
        Object data = null;
        try {
            // Nếu có chuỗi JSON, parse nó thành Java Object (Map/List)
            if (entity.getConfigJson() != null && !entity.getConfigJson().isBlank()) {
                data = mapper.readValue(entity.getConfigJson(), Object.class);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return new CustomizationConfigResponse(
                entity.getId(),
                entity.getStepType(),
                entity.isEnabled(),
                entity.getExtraPrice(),
                data
        );
    }
}
