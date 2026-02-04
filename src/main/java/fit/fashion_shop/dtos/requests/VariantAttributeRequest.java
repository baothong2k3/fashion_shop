/*
 * @ (#) VariantAttributeRequest.java    1.0    15/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 15/01/2026
 * @version: 1.0
 */

public record VariantAttributeRequest(
        String attributeName, // Ví dụ: "Color", "Size"
        String value,         // Ví dụ: "Red", "XL"
        String hexCode,       // Ví dụ: "#ff0000" (Optional, chỉ dùng khi tạo mới màu)
        String imageUrl       // Optional: Dùng nếu giá trị thuộc tính có hình ảnh riêng
) {}
