/*
 * @ (#) CartService.java    1.0    31/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.services;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.requests.AddToCartCustomizedRequest;
import fit.fashion_shop.dtos.requests.AddToCartRequest;
import fit.fashion_shop.dtos.responses.CartResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CartService {
    CartResponse addToCart(Long userId, AddToCartRequest request);
    CartResponse getMyCart(Long userId);
    CartResponse addToCartCustomized(Long userId, AddToCartCustomizedRequest request, List<MultipartFile> photoFiles);
}