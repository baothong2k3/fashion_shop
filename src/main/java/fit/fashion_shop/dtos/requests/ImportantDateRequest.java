/*
 * @ (#) ImportantDateRequest.java    1.0    09/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 09/01/2026
 * @version: 1.0
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ImportantDateRequest(
        @NotBlank(message = "Tiêu đề không được để trống")
        String title,

        @NotNull(message = "Ngày không được để trống")
        LocalDate date,

        Integer remindBeforeDays,

        boolean yearlyRepeat
) {}
