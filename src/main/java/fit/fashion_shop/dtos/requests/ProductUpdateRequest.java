/*
 * @ (#) ProductUpdateRequest.java    1.0    24/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 24/01/2026
 * @version: 1.0
 */

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductUpdateRequest(
        String name,

        @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug không hợp lệ")
        String slug,

        String description,

        @Positive(message = "Giá phải lớn hơn 0")
        Double price,

        @PositiveOrZero(message = "Giá khuyến mãi không được âm")
        Double salePrice,

        @PositiveOrZero(message = "Giảm giá không được âm")
        Integer discount,

        @PositiveOrZero(message = "Tồn kho không được âm")
        Integer stock,

        Boolean newProduct,
        Boolean featured,
        Boolean bestSeller,
        Boolean customizable,

        Long categoryId
) {}