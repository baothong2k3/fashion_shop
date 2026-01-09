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
import fit.fashion_shop.dtos.requests.AddressRequest;
import fit.fashion_shop.dtos.requests.ChangePasswordRequest;
import fit.fashion_shop.dtos.requests.ImportantDateRequest;
import fit.fashion_shop.dtos.requests.UpdateProfileRequest;
import fit.fashion_shop.dtos.responses.AddressResponse;
import fit.fashion_shop.dtos.responses.ImportantDateResponse;
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

import java.util.List;

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

    @PostMapping("/addresses")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> addAddress(
            @AuthenticationPrincipal User authUser,
            @Valid @RequestBody AddressRequest request,
            HttpServletRequest httpReq) {

        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.addAddress(authUser.getId(), request);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Thêm địa chỉ mới thành công",
                httpReq.getRequestURI()
        ));
    }

    @PutMapping("/addresses/{addressId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> updateAddress(
            @AuthenticationPrincipal User authUser,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request,
            HttpServletRequest httpReq) {

        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.updateAddress(authUser.getId(), addressId, request);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Cập nhật địa chỉ thành công",
                httpReq.getRequestURI()
        ));
    }

    @DeleteMapping("/addresses/{addressId}")
    @PreAuthorize("isAuthenticated()") // Yêu cầu đăng nhập
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @AuthenticationPrincipal User authUser, // Lấy User từ Token
            @PathVariable Long addressId,
            HttpServletRequest httpReq) {

        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.deleteAddress(authUser.getId(), addressId);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Xóa địa chỉ thành công",
                httpReq.getRequestURI()
        ));
    }

    @GetMapping("/addresses")
    @PreAuthorize("isAuthenticated()") // Yêu cầu người dùng đăng nhập
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddresses(
            @AuthenticationPrincipal User authUser, // Lấy thông tin user từ JWT
            HttpServletRequest httpReq) {

        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<AddressResponse> addresses = userService.getUserAddresses(authUser.getId());

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Lấy danh sách địa chỉ thành công",
                addresses,
                httpReq.getRequestURI()
        ));
    }

    @PostMapping("/important-dates")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> addImportantDates(
            @AuthenticationPrincipal User authUser,
            @Valid @RequestBody List<ImportantDateRequest> requests,
            HttpServletRequest httpReq) {

        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.addImportantDates(authUser.getId(), requests);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Thêm các ngày quan trọng thành công",
                httpReq.getRequestURI()
        ));
    }

    @PutMapping("/important-dates/{dateId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> updateImportantDate(
            @AuthenticationPrincipal User authUser,
            @PathVariable Long dateId,
            @Valid @RequestBody ImportantDateRequest request,
            HttpServletRequest httpReq) {

        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.updateImportantDate(authUser.getId(), dateId, request);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Cập nhật thông tin ngày quan trọng thành công",
                httpReq.getRequestURI()
        ));
    }

    @DeleteMapping("/important-dates/{dateId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> deleteImportantDate(
            @AuthenticationPrincipal User authUser,
            @PathVariable Long dateId,
            HttpServletRequest httpReq) {

        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.deleteImportantDate(authUser.getId(), dateId);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Xóa ngày quan trọng thành công",
                httpReq.getRequestURI()
        ));
    }

    @GetMapping("/important-dates")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ImportantDateResponse>>> getImportantDates(
            @AuthenticationPrincipal User authUser,
            HttpServletRequest httpReq) {

        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ImportantDateResponse> dates = userService.getUserImportantDates(authUser.getId());

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Lấy danh sách ngày quan trọng thành công",
                dates,
                httpReq.getRequestURI()
        ));
    }
}
