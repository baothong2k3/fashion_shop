/*
 * @ (#) AuthUser.java    1.0    04/02/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 04/02/2026
 * @version: 1.0
 */

import fit.fashion_shop.enums.Role;
import lombok.Builder;

@Builder
public record AuthUserResponse(
        Long id,
        String email,
        String fullName,
        Role role
) {}
