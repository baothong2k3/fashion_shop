/*
 * @ (#) ProductResponse.java    1.0    14/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 14/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.Product;

import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String name,
        String slug,
        String description,
        Double price,
        Double salePrice,
        Integer discount,
        String thumbnail,
        Integer stock,
        boolean newProduct,
        boolean featured,
        boolean bestSeller,
        boolean customizable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long categoryId
) {
    public static ProductResponse fromEntity(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getPrice(),
                product.getSalePrice(),
                product.getDiscount(),
                product.getThumbnail(),
                product.getStock(),
                product.isNewProduct(),
                product.isFeatured(),
                product.isBestSeller(),
                product.isCustomizable(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getCategory() != null ? product.getCategory().getId() : null
        );
    }
}