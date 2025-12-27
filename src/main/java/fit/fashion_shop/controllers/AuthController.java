/*
 * @ (#) AuthController.java    1.0    27/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.controllers;/*
 * @description:
 * @author: Bao Thong
 * @date: 27/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.dtos.ApiResponse;
import fit.fashion_shop.dtos.requests.*;
import fit.fashion_shop.dtos.responses.LoginResponse;
import fit.fashion_shop.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest servletRequest) {

        authService.register(request);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Đăng ký tạm thời thành công. Vui lòng kiểm tra email để lấy mã OTP.",
                servletRequest.getRequestURI()
        ));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(
            @RequestBody VerifyOtpRequest request,
            HttpServletRequest servletRequest) {

        authService.verifyRegistration(request.email(), request.otpCode());

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Xác thực tài khoản thành công. Bạn có thể đăng nhập.",
                servletRequest.getRequestURI()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest servletRequest) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Đăng nhập thành công",
                response,
                servletRequest.getRequestURI()
        ));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request,
            HttpServletRequest servletRequest) {

        authService.forgotPassword(request);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Mã OTP đặt lại mật khẩu đã được gửi vào email của bạn.",
                servletRequest.getRequestURI()
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request,
            HttpServletRequest servletRequest) {

        authService.resetPassword(request);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Đặt lại mật khẩu thành công. Bạn có thể đăng nhập bằng mật khẩu mới.",
                servletRequest.getRequestURI()
        ));
    }
}
