/*
 * @ (#) AddToCartRequest.java    1.0    31/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/01/2026
 * @version: 1.0
 */

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddToCartRequest(
        @NotNull(message = "ID sản phẩm không được để trống")
        Long productId,

        Long variantId, // Có thể null nếu sản phẩm không có biến thể

        @Min(value = 1, message = "Số lượng phải lớn hơn 0")
        @NotNull(message = "Số lượng không được để trống")
        Integer quantity
) {}
