/*
 * @ (#) VariantAttributeResponse.java    1.0    15/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 15/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.ProductVariantAttribute;

public record VariantAttributeResponse(
        String name,      // Tên thuộc tính (Color, Size)
        String value,     // Giá trị (Red, XL)
        String hexCode,   // #FF0000 (nếu có)
        String imageUrl   // URL ảnh (nếu có)
) {
    public static VariantAttributeResponse fromEntity(ProductVariantAttribute entity) {
        return new VariantAttributeResponse(
                entity.getAttributeValue().getAttribute().getName(),
                entity.getAttributeValue().getValue(),
                entity.getAttributeValue().getHexCode(),
                entity.getAttributeValue().getImageUrl()
        );
    }
}
