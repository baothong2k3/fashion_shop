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
import fit.fashion_shop.dtos.requests.ImportantDateRequest;
import fit.fashion_shop.dtos.requests.UpdateProfileRequest;
import fit.fashion_shop.dtos.responses.AddressResponse;
import fit.fashion_shop.dtos.responses.UserResponse;
import fit.fashion_shop.entities.Address;
import fit.fashion_shop.entities.ImportantDate;
import fit.fashion_shop.entities.User;
import fit.fashion_shop.exceptions.DuplicateResourceException;
import fit.fashion_shop.exceptions.OperationNotPermittedException;
import fit.fashion_shop.exceptions.PasswordValidationException;
import fit.fashion_shop.exceptions.ResourceNotFoundException;
import fit.fashion_shop.repositories.AddressRepository;
import fit.fashion_shop.repositories.ImportantDateRepository;
import fit.fashion_shop.repositories.UserRepository;
import fit.fashion_shop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImportantDateRepository importantDateRepository;

    @Override
    public UserResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        return UserResponse.fromUser(user);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        // 1. Load lại user từ DB để đảm bảo dữ liệu mới nhất
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

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
                throw new DuplicateResourceException("Số điện thoại đã được sử dụng bởi người dùng khác");
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
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // 2. Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new PasswordValidationException("Mật khẩu cũ không chính xác");
        }

        // 3. Kiểm tra mật khẩu mới và xác nhận mật khẩu
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new PasswordValidationException("Mật khẩu xác nhận không khớp");
        }

        // Kiểm tra mật khẩu mới không được trùng mật khẩu cũ
        if (request.newPassword().equals(request.oldPassword())) {
            throw new PasswordValidationException("Mật khẩu mới không được trùng với mật khẩu cũ");
        }

        // 4. Mã hóa và cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void addAddress(Long userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // Nếu địa chỉ mới là mặc định, reset các địa chỉ cũ của user này
        if (request.defaultAddress()) {
            addressRepository.resetDefaultAddress(userId);
        }

        Address address = Address.builder()
                .recipientName(request.recipientName())
                .phoneNumber(request.phoneNumber())
                .street(request.street())
                .city(request.city())
                .district(request.district())
                .ward(request.ward())
                .defaultAddress(request.defaultAddress())
                .user(user)
                .build();

        addressRepository.save(address);
    }

    @Override
    @Transactional
    public void updateAddress(Long userId, Long addressId, AddressRequest request) {
        // 1. Lấy tất cả địa chỉ của người dùng
        List<Address> allAddresses = addressRepository.findByUserId(userId);

        Address addressToUpdate = allAddresses.stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ hoặc bạn không có quyền chỉnh sửa"));

        boolean newDefaultStatus = request.defaultAddress();

        // Ràng buộc 1: Nếu là địa chỉ duy nhất, luôn buộc phải là mặc định
        if (allAddresses.size() == 1) {
            newDefaultStatus = true;
        }
        // Ràng buộc 2: Nếu địa chỉ đang sửa là mặc định và người dùng muốn bỏ chọn mặc định
        else if (addressToUpdate.isDefaultAddress() && !request.defaultAddress()) {
            // Tìm một địa chỉ khác (không phải địa chỉ đang sửa) để gán làm mặc định thay thế
            Address successor = allAddresses.stream()
                    .filter(a -> !a.getId().equals(addressId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Lỗi hệ thống: Không tìm thấy địa chỉ thay thế"));

            successor.setDefaultAddress(true);
            addressRepository.save(successor);
        }
        // Ràng buộc 3: Nếu người dùng chọn địa chỉ này làm mặc định mới
        else if (newDefaultStatus) {
            // Reset các địa chỉ khác về false trước khi set cái này thành true
            addressRepository.resetDefaultAddress(userId);
        }

        // 2. Cập nhật các thông tin từ request vào Entity
        addressToUpdate.setRecipientName(request.recipientName());
        addressToUpdate.setPhoneNumber(request.phoneNumber());
        addressToUpdate.setStreet(request.street());
        addressToUpdate.setCity(request.city());
        addressToUpdate.setDistrict(request.district());
        addressToUpdate.setWard(request.ward());
        addressToUpdate.setDefaultAddress(newDefaultStatus);

        addressRepository.save(addressToUpdate);
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        // 1. Lấy tất cả địa chỉ của người dùng để kiểm tra các ràng buộc
        List<Address> userAddresses = addressRepository.findByUserId(userId);

        // 2. Tìm địa chỉ cần xóa trong danh sách (để đảm bảo quyền sở hữu)
        Address addressToDelete = userAddresses.stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ"));

        // 3. Ràng buộc: Nếu chỉ có 1 địa chỉ duy nhất thì không cho phép xóa
        if (userAddresses.size() == 1) {
            throw new OperationNotPermittedException("Không thể xóa địa chỉ duy nhất. Bạn phải có ít nhất một địa chỉ giao hàng.");
        }

        // 4. Nếu địa chỉ cần xóa là địa chỉ mặc định, hãy chọn một địa chỉ khác làm mặc định mới
        if (addressToDelete.isDefaultAddress()) {
            // Tìm địa chỉ đầu tiên không phải là địa chỉ sắp xóa
            Address newDefaultAddress = userAddresses.stream()
                    .filter(a -> !a.getId().equals(addressId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Lỗi hệ thống: Không tìm thấy địa chỉ thay thế"));

            newDefaultAddress.setDefaultAddress(true);
            addressRepository.save(newDefaultAddress);
        }

        // 5. Thực hiện xóa địa chỉ
        addressRepository.delete(addressToDelete);
    }

    @Override
    public List<AddressResponse> getUserAddresses(Long userId) {
        // Kiểm tra user tồn tại
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Không tìm thấy người dùng");
        }

        // Lấy danh sách địa chỉ và map sang AddressResponse
        return addressRepository.findByUserIdOrderByDefaultAddressDescIdDesc(userId)
                .stream()
                .map(AddressResponse::fromAddress)
                .toList();
    }

    @Override
    @Transactional
    public void addImportantDates(Long userId, List<ImportantDateRequest> requests) {
        // 1. Kiểm tra User tồn tại
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // 2. Chuyển đổi từ DTO sang Entity và lưu
        List<ImportantDate> dates = requests.stream()
                .map(req -> ImportantDate.builder()
                        .title(req.title())
                        .date(req.date())
                        .remindBeforeDays(req.remindBeforeDays() != null ? req.remindBeforeDays() : 7)
                        .yearlyRepeat(req.yearlyRepeat())
                        .active(true)
                        .user(user)
                        .build())
                .toList();

        importantDateRepository.saveAll(dates);
    }

    @Override
    @Transactional
    public void updateImportantDate(Long userId, Long dateId, ImportantDateRequest request) {
        // 1. Kiểm tra sự tồn tại và quyền sở hữu
        ImportantDate importantDate = importantDateRepository.findByIdAndUserId(dateId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngày quan trọng hoặc bạn không có quyền chỉnh sửa"));

        // 2. Cập nhật thông tin
        importantDate.setTitle(request.title());
        importantDate.setDate(request.date());
        importantDate.setRemindBeforeDays(request.remindBeforeDays() != null ? request.remindBeforeDays() : 7);
        importantDate.setYearlyRepeat(request.yearlyRepeat());

        // 3. Lưu (Hibernate sẽ tự động update nhờ @Transactional)
        importantDateRepository.save(importantDate);
    }
}
