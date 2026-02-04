/*
 * @ (#) UpdateVariantRequest.java    1.0    15/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 15/01/2026
 * @version: 1.0
 */

import jakarta.validation.constraints.Min;
import java.util.List;

public record UpdateVariantRequest(
        String sku,

        @Min(value = 0, message = "Giá không được âm")
        Double priceOverride,

        @Min(value = 0, message = "Tồn kho không được âm")
        Integer stock,

        List<VariantAttributeRequest> attributes // Tái sử dụng DTO VariantAttributeRequest cũ
) {}
