/*
 * @ (#) OtpService.java    1.0    27/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.services;/*
 * @description:
 * @author: Bao Thong
 * @date: 27/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.entities.Otp;
import fit.fashion_shop.enums.OtpType;
import fit.fashion_shop.repositories.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;

    @Transactional
    public String generateOtp(String email, OtpType type) {
        // Xóa mã cũ nếu có
        otpRepository.deleteByEmailAndType(email, type);

        String code = String.format("%06d", new Random().nextInt(1000000));
        Otp otp = Otp.builder()
                .email(email)
                .otpCode(code)
                .type(type)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .build();
        otpRepository.save(otp);
        return code;
    }

    public boolean validateOtp(String email, String code, OtpType type) {
        return otpRepository.findByEmailAndOtpCodeAndType(email, code, type)
                .filter(otp -> otp.getExpiryTime().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}
