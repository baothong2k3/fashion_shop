/*
 * @ (#) CustomizationConfigRequest.java    1.0    22/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 22/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.enums.StepType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record CustomizationConfigRequest(
        @NotNull(message = "Loại bước (StepType) không được để trống")
        StepType stepType,

        boolean enabled,

        @Min(value = 0, message = "Giá cộng thêm không được âm")
        Double extraPrice,

        // Nhận Map để linh động, sau đó Service sẽ convert sang JSON String
        Map<String, Object> configData
) {}
