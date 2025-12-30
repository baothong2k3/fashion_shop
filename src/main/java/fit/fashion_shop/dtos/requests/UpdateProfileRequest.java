/*
 * @ (#) UpdateProfileRequest.java    1.0    30/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 30/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.enums.Gender;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record UpdateProfileRequest(
        String fullName,

        Gender gender,

        @Past(message = "Ngày sinh phải là ngày trong quá khứ")
        LocalDate dateOfBirth,

        // Cho phép ký tự số, độ dài 10-15 ký tự, có thể có dấu + ở đầu
        // Regex này chấp nhận cả số VN (09xxx) và quốc tế (+84xxx, +1xxx)
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Số điện thoại không hợp lệ")
        String phoneNumber
) {}
