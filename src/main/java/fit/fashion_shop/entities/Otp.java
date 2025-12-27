/*
 * @ (#) Otp.java    1.0    27/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.entities;/*
 * @description:
 * @author: Bao Thong
 * @date: 27/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.enums.OtpType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "otps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email; // Định danh người nhận OTP

    private String otpCode;

    @Enumerated(EnumType.STRING)
    private OtpType type; // Phân loại mục đích OTP

    private LocalDateTime expiryTime;
}
