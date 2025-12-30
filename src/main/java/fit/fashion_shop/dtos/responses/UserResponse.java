/*
 * @ (#) UserResponse.java    1.0    30/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 30/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.entities.User;
import fit.fashion_shop.enums.Gender;
import fit.fashion_shop.enums.Role;
import java.time.LocalDate;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        String phoneNumber,
        LocalDate dateOfBirth,
        Gender gender,
        Role role
) {
    // Helper method để map từ Entity sang Response
    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getRole()
        );
    }
}
