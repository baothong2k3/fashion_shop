/*
 * @ (#) ProductDetailResponse.java    1.0    23/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 23/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.Product;
import fit.fashion_shop.entities.ProductCustomizationConfig;
import fit.fashion_shop.entities.ProductVariant;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public record ProductDetailResponse(
        ProductResponse product,                      // Thông tin chung (Tên, giá, ảnh...)
        List<ProductVariantResponse> variants,        // Danh sách biến thể (Size, Color...)
        List<CustomizationConfigResponse> customizationConfigs // Cấu hình thiết kế (nếu có)
) {
    public static ProductDetailResponse fromEntity(
            Product product,
            List<ProductVariant> variants,
            List<ProductCustomizationConfig> configs,
            ObjectMapper mapper
    ) {
        return new ProductDetailResponse(
                ProductResponse.fromEntity(product),
                // Map danh sách variants sang DTO
                variants.stream().map(ProductVariantResponse::fromEntity).toList(),
                // Map danh sách configs sang DTO
                configs.stream().map(c -> CustomizationConfigResponse.fromEntity(c, mapper)).toList()
        );
    }
}
