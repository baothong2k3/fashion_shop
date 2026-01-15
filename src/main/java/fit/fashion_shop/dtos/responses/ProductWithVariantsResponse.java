/*
 * @ (#) ProductWithVariantsResponse.java    1.0    15/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 15/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.Product;
import fit.fashion_shop.entities.ProductVariant;

import java.util.List;

public record ProductWithVariantsResponse(
        ProductResponse product,              // Thông tin sản phẩm gốc
        List<ProductVariantResponse> variants // Danh sách toàn bộ biến thể
) {
    public static ProductWithVariantsResponse fromEntity(Product product, List<ProductVariant> variants) {
        return new ProductWithVariantsResponse(
                ProductResponse.fromEntity(product),
                variants.stream().map(ProductVariantResponse::fromEntity).toList()
        );
    }
}
