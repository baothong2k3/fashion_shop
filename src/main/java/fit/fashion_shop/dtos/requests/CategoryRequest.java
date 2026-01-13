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

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "Tên danh mục không được để trống")
        String name,

        @NotBlank(message = "Slug không được để trống")
        String slug,

        String description,
        Long parentId,
        int sortOrder,
        boolean isActive,
        MultipartFile imageFile,
        MultipartFile iconFile
) {}