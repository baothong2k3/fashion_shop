/*
 * @ (#) User.java    1.0    27/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.entities;/*
 * @description:
 * @author: Bao Thong
 * @date: 27/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import fit.fashion_shop.enums.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled; // Tài khoản chỉ active sau khi verify OTP

    // Quan hệ 1-N với Address
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();
}
