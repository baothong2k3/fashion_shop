/*
 * @ (#) CartResponse.java    1.0    31/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.Cart;
import fit.fashion_shop.entities.CartItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record CartResponse(
        Long id,
        List<CartItemResponse> items,
        Double grandTotal,
        Integer totalItems
) {
    public static CartResponse fromEntity(Cart cart) {
        if (cart == null) {
            return new CartResponse(null, new ArrayList<>(), 0.0, 0);
        }

        // Lấy danh sách item và sắp xếp theo ID giảm dần (Mới nhất lên đầu)
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .sorted(Comparator.comparing(CartItem::getId).reversed())
                .map(CartItemResponse::fromEntity)
                .toList();

        double total = itemResponses.stream().mapToDouble(CartItemResponse::totalPrice).sum();

        return new CartResponse(
                cart.getId(),
                itemResponses,
                total,
                itemResponses.size()
        );
    }
}
