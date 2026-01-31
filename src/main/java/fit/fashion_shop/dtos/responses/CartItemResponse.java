/*
 * @ (#) CartItemResponse.java    1.0    31/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;

import fit.fashion_shop.entities.CartItem;

public record CartItemResponse(
        Long id,
        Long productId,
        String productName,
        String productThumbnail,
        Long variantId,
        String variantSku,
        Double price,
        Integer quantity,
        Double totalPrice,
        CustomizedSelectionResponse customization
) {
    public static CartItemResponse fromEntity(CartItem item) {
        Double price;
        String sku = null;
        String thumbnail = item.getProduct().getThumbnail();
        CustomizedSelectionResponse customizationResponse = null;

        // 1. Xử lý Customize
        if (item.getSelection() != null) {
            double basePrice = item.getProduct().getSalePrice() != null
                    ? item.getProduct().getSalePrice()
                    : item.getProduct().getPrice();

            double extraFee = item.getSelection().getTotalCustomPrice() != null
                    ? item.getSelection().getTotalCustomPrice()
                    : 0.0;

            price = basePrice + extraFee;
            sku = "CUSTOM-" + item.getSelection().getBagType();

            // Map Entity sang DTO chi tiết
            customizationResponse = CustomizedSelectionResponse.fromEntity(item.getSelection());
        }
        // 2. Xử lý Variant
        else if (item.getVariant() != null) {
            price = item.getVariant().getPriceOverride();
            sku = item.getVariant().getSku();
            if (item.getVariant().getThumbnail() != null) {
                thumbnail = item.getVariant().getThumbnail();
            }
        }
        // 3. Sản phẩm thường
        else {
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
                price * item.getQuantity(),
                customizationResponse // Truyền dữ liệu chi tiết vào
        );
    }
}