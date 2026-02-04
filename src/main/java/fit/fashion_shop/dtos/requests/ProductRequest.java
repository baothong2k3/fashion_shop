/*
 * @ (#) ProductRequest.java    1.0    14/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 14/01/2026
 * @version: 1.0
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
        @NotBlank(message = "Tên sản phẩm không được để trống")
        String name,

        @NotBlank(message = "Slug không được để trống")
        String slug,

        String description,

        @NotNull(message = "Giá không được để trống")
        @Positive(message = "Giá phải lớn hơn 0")
        Double price,

        Double salePrice,
        Integer discount,

        @NotNull(message = "Số lượng tồn kho không được để trống")
        Integer stock,

        Boolean newProduct,
        Boolean featured,
        Boolean bestSeller,
        Boolean customizable,

        @NotNull(message = "ID danh mục không được để trống")
        Long categoryId
) {}
