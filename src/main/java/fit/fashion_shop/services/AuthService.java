/*
 * @ (#) AuthService.java    1.0    27/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.services;/*
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

public interface AuthService {
    void register(RegisterRequest request);
    void verifyRegistration(String email, String code);
    LoginResponse login(LoginRequest request);
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
}
