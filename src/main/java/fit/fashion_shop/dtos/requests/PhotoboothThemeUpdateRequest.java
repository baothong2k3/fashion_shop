/*
 * @ (#) PhotoboothThemeUpdateRequest.java    1.0    24/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 24/01/2026
 * @version: 1.0
 */

import jakarta.validation.constraints.Min;
import org.springframework.web.multipart.MultipartFile;

public record PhotoboothThemeUpdateRequest(
        String name,

        @Min(value = 1, message = "Số lượng slot phải ít nhất là 1")
        Integer slotsCount,

        MultipartFile imageFile
) {}