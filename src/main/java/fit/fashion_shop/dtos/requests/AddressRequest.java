/*
 * @ (#) AddressRequest.java    1.0    31/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/12/2025
 * @version: 1.0
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddressRequest(
        @NotBlank(message = "Tên người nhận không được để trống")
        String recipientName,

        @NotBlank(message = "Số điện thoại không được để trống")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Số điện thoại không hợp lệ")
        String phoneNumber,

        @NotBlank(message = "Số nhà, tên đường không được để trống")
        String street,

        @NotBlank(message = "Tỉnh/Thành phố không được để trống")
        String city,

        @NotBlank(message = "Quận/Huyện không được để trống")
        String district,

        @NotBlank(message = "Phường/Xã không được để trống")
        String ward,

        boolean isDefault
) {}
