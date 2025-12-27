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

import fit.fashion_shop.dtos.requests.RegisterRequest;
import fit.fashion_shop.entities.User;
import fit.fashion_shop.enums.OtpType;
import fit.fashion_shop.enums.Role;
import fit.fashion_shop.repositories.UserRepository;
import fit.fashion_shop.services.AuthService;
import fit.fashion_shop.services.MailService;
import fit.fashion_shop.services.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

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
        if (!otpService.validateOtp(email, code, OtpType.REGISTER)) {
            throw new RuntimeException("Mã OTP không hợp lệ hoặc đã hết hạn");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        user.setEnabled(true);
        userRepository.save(user);
    }
}
