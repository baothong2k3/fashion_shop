/*
 * @ (#) PhotoboothThemeRequest.java    1.0    24/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 24/01/2026
 * @version: 1.0
 */

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record PhotoboothThemeRequest(
        @NotBlank(message = "Tên chủ đề không được để trống")
        String name,

        @NotNull(message = "Số lượng slot ảnh không được để trống")
        @Min(value = 1, message = "Số lượng slot phải ít nhất là 1")
        Integer slotsCount,

        @NotNull(message = "Ảnh xem trước không được để trống")
        MultipartFile imageFile
) {}
