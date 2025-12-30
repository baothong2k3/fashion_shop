/*
 * @ (#) UserService.java    1.0    30/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.services;/*
 * @description:
 * @author: Bao Thong
 * @date: 30/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.dtos.requests.AddressRequest;
import fit.fashion_shop.dtos.requests.ChangePasswordRequest;
import fit.fashion_shop.dtos.requests.UpdateProfileRequest;
import fit.fashion_shop.dtos.responses.UserResponse;

public interface UserService {
    UserResponse getProfile(Long userId);
    UserResponse updateProfile(Long userId, UpdateProfileRequest request);
    void changePassword(Long userId, ChangePasswordRequest request);
    void addAddress(Long userId, AddressRequest request);
    void updateAddress(Long userId, Long addressId, AddressRequest request);
    void deleteAddress(Long userId, Long addressId);
}
