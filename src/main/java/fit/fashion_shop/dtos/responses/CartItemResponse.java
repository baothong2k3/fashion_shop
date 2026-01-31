/*
 * @ (#) CartItemResponse.java    1.0    31/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.CartItem;

public record CartItemResponse(
        Long id,
        Long productId,
        String productName,
        String productThumbnail,
        Long variantId,
        String variantSku, // SKU của biến thể nếu có
        Double price,      // Giá tại thời điểm hiện tại
        Integer quantity,
        Double totalPrice
) {
    public static CartItemResponse fromEntity(CartItem item) {
        // Xác định giá và thông tin hiển thị dựa trên việc có biến thể hay không
        Double price;
        String sku = null;
        String thumbnail = item.getProduct().getThumbnail();

        if (item.getVariant() != null) {
            price = item.getVariant().getPriceOverride();
            sku = item.getVariant().getSku();
            if (item.getVariant().getThumbnail() != null) {
                thumbnail = item.getVariant().getThumbnail();
            }
        } else {
            price = item.getProduct().getSalePrice() != null
                    ? item.getProduct().getSalePrice()
                    : item.getProduct().getPrice();
        }

        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                thumbnail,
                item.getVariant() != null ? item.getVariant().getId() : null,
                sku,
                price,
                item.getQuantity(),
                price * item.getQuantity()
        );
    }
}