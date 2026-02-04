/*
 * @ (#) ResendOtpRequest.java    1.0    29/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 29/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.enums.OtpType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResendOtpRequest(
        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không hợp lệ")
        String email,

        @NotNull(message = "Loại OTP không được để trống")
        OtpType type
) {}