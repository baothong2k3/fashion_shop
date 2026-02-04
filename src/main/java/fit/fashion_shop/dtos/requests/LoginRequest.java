/*
 * @ (#) LoginRequest.java    1.0    27/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 27/12/2025
 * @version: 1.0
 */

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không hợp lệ")
        String email,

        @NotBlank(message = "Mật khẩu không được để trống")
        String password
) {}
