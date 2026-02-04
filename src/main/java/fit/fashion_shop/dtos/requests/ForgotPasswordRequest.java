/*
 * @ (#) ForgotPasswordRequest.java    1.0    28/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 28/12/2025
 * @version: 1.0
 */

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không hợp lệ")
        String email
) {}
