/*
 * @ (#) LoginResponse.java    1.0    27/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 27/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.enums.Role;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String email,
        String fullName,
        Role role
) {}
