/*
 * @ (#) ProductWithCustomizationResponse.java    1.0    22/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 22/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.Product;
import fit.fashion_shop.entities.ProductCustomizationConfig;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public record ProductWithCustomizationResponse(
        ProductResponse product,
        List<CustomizationConfigResponse> customizationConfigs
) {
    public static ProductWithCustomizationResponse fromEntity(
            Product product,
            List<ProductCustomizationConfig> configs,
            ObjectMapper mapper
    ) {
        return new ProductWithCustomizationResponse(
                ProductResponse.fromEntity(product),
                configs.stream()
                        .map(c -> CustomizationConfigResponse.fromEntity(c, mapper))
                        .toList()
        );
    }
}