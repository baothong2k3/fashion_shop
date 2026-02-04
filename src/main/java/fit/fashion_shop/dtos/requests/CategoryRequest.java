/*
 * @ (#) CategoryRequest.java    1.0    13/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 13/01/2026
 * @version: 1.0
 */

import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "Tên danh mục không được để trống")
        String name,

        @NotBlank(message = "Slug không được để trống")
        @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug không hợp lệ")
        String slug,

        String description,
        Long parentId,
        Integer sortOrder,
        Boolean isActive,
        MultipartFile imageFile,
        MultipartFile iconFile
) {}