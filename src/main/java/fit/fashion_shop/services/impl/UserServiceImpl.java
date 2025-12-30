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

import fit.fashion_shop.dtos.requests.AddressRequest;
import fit.fashion_shop.dtos.requests.ChangePasswordRequest;
import fit.fashion_shop.dtos.requests.UpdateProfileRequest;
import fit.fashion_shop.dtos.responses.UserResponse;
import fit.fashion_shop.entities.Address;
import fit.fashion_shop.entities.User;
import fit.fashion_shop.repositories.AddressRepository;
import fit.fashion_shop.repositories.UserRepository;
import fit.fashion_shop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        // 1. Tìm user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // 2. Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác");
        }

        // 3. Kiểm tra mật khẩu mới và xác nhận mật khẩu
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp");
        }

        // Kiểm tra mật khẩu mới không được trùng mật khẩu cũ
        if (request.newPassword().equals(request.oldPassword())) {
            throw new RuntimeException("Mật khẩu mới không được trùng với mật khẩu cũ");
        }

        // 4. Mã hóa và cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void addAddress(Long userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Nếu địa chỉ mới là mặc định, reset các địa chỉ cũ của user này
        if (request.isDefault()) {
            addressRepository.resetDefaultAddress(userId);
        }

        Address address = Address.builder()
                .recipientName(request.recipientName())
                .phoneNumber(request.phoneNumber())
                .street(request.street())
                .city(request.city())
                .district(request.district())
                .ward(request.ward())
                .isDefault(request.isDefault())
                .user(user)
                .build();

        addressRepository.save(address);
    }

    @Override
    @Transactional
    public void updateAddress(Long userId, Long addressId, AddressRequest request) {
        // 1. Tìm địa chỉ và kiểm tra quyền sở hữu
        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ hoặc bạn không có quyền chỉnh sửa"));

        // 2. Nếu đặt làm mặc định, reset các địa chỉ khác của người dùng
        if (request.isDefault()) {
            addressRepository.resetDefaultAddress(userId);
        }

        // 3. Cập nhật thông tin mới
        address.setRecipientName(request.recipientName());
        address.setPhoneNumber(request.phoneNumber());
        address.setStreet(request.street());
        address.setCity(request.city());
        address.setDistrict(request.district());
        address.setWard(request.ward());
        address.setDefault(request.isDefault());

        addressRepository.save(address);
    }
}
