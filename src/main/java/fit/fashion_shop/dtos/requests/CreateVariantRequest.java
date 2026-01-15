/*
 * @ (#) CreateVariantRequest.java    1.0    15/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 15/01/2026
 * @version: 1.0
 */

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreateVariantRequest(
        @NotBlank(message = "SKU không được để trống")
        String sku,

        @Min(value = 0, message = "Giá không được âm")
        Double priceOverride, // Giá riêng cho biến thể này (nếu khác giá gốc)

        @Min(value = 0, message = "Tồn kho không được âm")
        Integer stock,

        String thumbnail, // URL ảnh của biến thể (nếu có)

        List<VariantAttributeRequest> attributes // Danh sách thuộc tính: [Color:Red, Size:XL]
) {}
