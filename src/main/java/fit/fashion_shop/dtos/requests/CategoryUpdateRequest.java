/*
 * @ (#) CategoryUpdateRequest.java    1.0    13/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 13/01/2026
 * @version: 1.0
 */

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.Pattern;

public record CategoryUpdateRequest(
        String name,

        @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug không hợp lệ")
        String slug,

        String description,
        Long parentId,
        Integer sortOrder,
        Boolean isActive,
        MultipartFile imageFile,
        MultipartFile iconFile
) {
}
