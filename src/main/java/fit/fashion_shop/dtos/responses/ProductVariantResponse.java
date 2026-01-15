/*
 * @ (#) ProductVariantResponse.java    1.0    15/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 15/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.ProductVariant;
import java.util.List;

public record ProductVariantResponse(
        Long id,
        String sku,
        Double priceOverride,
        Integer stock,
        String thumbnail,
        List<VariantAttributeResponse> attributes
) {
    public static ProductVariantResponse fromEntity(ProductVariant variant) {
        return new ProductVariantResponse(
                variant.getId(),
                variant.getSku(),
                variant.getPriceOverride(),
                variant.getStock(),
                variant.getThumbnail(),
                variant.getVariantAttributes() != null ?
                        variant.getVariantAttributes().stream()
                                .map(VariantAttributeResponse::fromEntity)
                                .toList() : List.of()
        );
    }
}
