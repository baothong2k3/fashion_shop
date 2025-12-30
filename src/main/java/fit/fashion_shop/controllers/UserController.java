/*
 * @ (#) UserController.java    1.0    30/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.controllers;/*
 * @description:
 * @author: Bao Thong
 * @date: 30/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.dtos.ApiResponse;
import fit.fashion_shop.dtos.requests.ChangePasswordRequest;
import fit.fashion_shop.dtos.requests.UpdateProfileRequest;
import fit.fashion_shop.dtos.responses.UserResponse;
import fit.fashion_shop.entities.User;
import fit.fashion_shop.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(
            @AuthenticationPrincipal User authUser,
            HttpServletRequest httpReq) {

        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserResponse userResponse = userService.getProfile(authUser.getId());

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Lấy thông tin cá nhân thành công",
                userResponse,
                httpReq.getRequestURI()
        ));
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @AuthenticationPrincipal User authUser, // Lấy user từ Token
            @Valid @RequestBody UpdateProfileRequest request, // Validate input
            HttpServletRequest httpReq) {

        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Gọi Service, truyền ID lấy từ Token
        UserResponse updatedUser = userService.updateProfile(authUser.getId(), request);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Cập nhật thông tin thành công",
                updatedUser,
                httpReq.getRequestURI()
        ));
    }

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal User authUser,
            @Valid @RequestBody ChangePasswordRequest request,
            HttpServletRequest httpReq) {

        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.changePassword(authUser.getId(), request);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Đổi mật khẩu thành công",
                httpReq.getRequestURI()
        ));
    }
}
