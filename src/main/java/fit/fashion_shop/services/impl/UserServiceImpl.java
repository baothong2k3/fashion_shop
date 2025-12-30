/*
 * @ (#) UserServiceImpl.java    1.0    30/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.services.impl;/*
 * @description:
 * @author: Bao Thong
 * @date: 30/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.dtos.requests.UpdateProfileRequest;
import fit.fashion_shop.dtos.responses.UserResponse;
import fit.fashion_shop.entities.User;
import fit.fashion_shop.repositories.UserRepository;
import fit.fashion_shop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        return UserResponse.fromUser(user);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        // 1. Load lại user từ DB để đảm bảo dữ liệu mới nhất (Entity Managed State)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Partial Update: Chỉ set giá trị nếu request có gửi lên (khác null & khác rỗng)

        // Cập nhật FullName
        if (request.fullName() != null && !request.fullName().isBlank()) {
            user.setFullName(request.fullName().trim());
        }

        // Cập nhật Gender
        if (request.gender() != null) {
            user.setGender(request.gender());
        }

        // Cập nhật DateOfBirth
        if (request.dateOfBirth() != null) {
            user.setDateOfBirth(request.dateOfBirth());
        }

        // Cập nhật PhoneNumber
        if (request.phoneNumber() != null && !request.phoneNumber().isBlank()) {
            String newPhone = request.phoneNumber().trim();
            // Kiểm tra số điện thoại đã tồn tại chưa
            if (!newPhone.equals(user.getPhoneNumber()) && userRepository.existsByPhoneNumber(newPhone)) {
                throw new RuntimeException("Số điện thoại đã được sử dụng");
            }
            user.setPhoneNumber(newPhone);
        }

        // 3. Save & Return
        // Vì đang trong @Transactional, Hibernate tự động detect thay đổi và update,
        // nhưng gọi save() rõ ràng giúp code dễ đọc hơn.
        User updatedUser = userRepository.save(user);

        return UserResponse.fromUser(updatedUser);
    }
}
