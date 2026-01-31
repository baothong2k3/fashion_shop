/*
 * @ (#) CartController.java    1.0    31/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.controllers;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.ApiResponse;
import fit.fashion_shop.dtos.requests.AddToCartRequest;
import fit.fashion_shop.dtos.responses.CartResponse;
import fit.fashion_shop.entities.User;
import fit.fashion_shop.services.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @AuthenticationPrincipal User user, // Lấy User từ Token
            @Valid @RequestBody AddToCartRequest request,
            HttpServletRequest httpReq) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CartResponse response = cartService.addToCart(user.getId(), request);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Thêm vào giỏ hàng thành công",
                response,
                httpReq.getRequestURI()
        ));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CartResponse>> getMyCart(
            @AuthenticationPrincipal User user,
            HttpServletRequest httpReq) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            CartResponse response = cartService.getMyCart(user.getId());
            return ResponseEntity.ok(ApiResponse.success(
                    HttpStatus.OK.value(),
                    "Lấy thông tin giỏ hàng thành công",
                    response,
                    httpReq.getRequestURI()
            ));
        } catch (Exception e) {
            // Trường hợp chưa có giỏ hàng
            return ResponseEntity.ok(ApiResponse.success(
                    HttpStatus.OK.value(),
                    "Giỏ hàng trống",
                    null,
                    httpReq.getRequestURI()
            ));
        }
    }
}
