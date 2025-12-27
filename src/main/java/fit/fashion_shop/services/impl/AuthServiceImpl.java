/*
 * @ (#) AuthServiceImpl.java    1.0    27/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.services.impl;/*
 * @description:
 * @author: Bao Thong
 * @date: 27/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.dtos.requests.ForgotPasswordRequest;
import fit.fashion_shop.dtos.requests.LoginRequest;
import fit.fashion_shop.dtos.requests.RegisterRequest;
import fit.fashion_shop.dtos.requests.ResetPasswordRequest;
import fit.fashion_shop.dtos.responses.LoginResponse;
import fit.fashion_shop.entities.User;
import fit.fashion_shop.enums.OtpType;
import fit.fashion_shop.enums.Role;
import fit.fashion_shop.exceptions.AccountNotEnabledException;
import fit.fashion_shop.exceptions.InvalidCredentialsException;
import fit.fashion_shop.repositories.UserRepository;
import fit.fashion_shop.services.AuthService;
import fit.fashion_shop.services.JwtService;
import fit.fashion_shop.services.MailService;
import fit.fashion_shop.services.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email đã được sử dụng");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .role(Role.CUSTOMER)
                .enabled(false) // Đợi xác thực OTP
                .build();
        userRepository.save(user);

        String otp = otpService.generateOtp(request.email(), OtpType.REGISTER);
        mailService.sendOtpMail(request.email(), otp);
    }

    @Override
    public void verifyRegistration(String email, String code) {

        // 1. Kiểm tra OTP
        if (!otpService.validateOtp(email, code, OtpType.REGISTER)) {
            throw new RuntimeException("Mã OTP không hợp lệ hoặc đã hết hạn");
        }

        // 2. Kích hoạt tài khoản
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        user.setEnabled(true);
        userRepository.save(user);

        // 3. Xóa OTP sau khi dùng xong để bảo mật
        otpService.deleteOtp(email, code, OtpType.REGISTER);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. Kiểm tra user tồn tại
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Email hoặc mật khẩu không chính xác"));

        // 2. Kiểm tra tài khoản đã được kích hoạt (Verify OTP) chưa
        if (!user.isEnabled()) {
            throw new AccountNotEnabledException("Tài khoản chưa được kích hoạt. Vui lòng xác thực OTP.");
        }

        // 3. Kiểm tra mật khẩu
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Email hoặc mật khẩu không chính xác");
        }

        // 4. Tạo bộ đôi Token
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken,
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        // 1. Kiểm tra email có tồn tại không
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống"));

        // 2. Tạo OTP với type FORGOT_PASSWORD (đã có trong Enum OtpType)
        String otp = otpService.generateOtp(user.getEmail(), OtpType.FORGOT_PASSWORD);

        // 3. Gửi mail
        mailService.sendForgotPasswordMail(user.getEmail(), otp);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // 1. Xác thực OTP
        if (!otpService.validateOtp(request.email(), request.otpCode(), OtpType.FORGOT_PASSWORD)) {
            throw new RuntimeException("Mã OTP không hợp lệ hoặc đã hết hạn");
        }

        // 2. Tìm User
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // 3. Cập nhật mật khẩu mới (mã hóa bằng BCrypt)
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        // 4. Xóa OTP sau khi đổi mật khẩu thành công
        otpService.deleteOtp(request.email(), request.otpCode(), OtpType.FORGOT_PASSWORD);
    }
}
