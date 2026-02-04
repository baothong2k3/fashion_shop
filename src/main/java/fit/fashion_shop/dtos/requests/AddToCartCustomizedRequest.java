/*
 * @ (#) AddToCartCustomizedRequest.java    1.0    31/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.enums.BagType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record AddToCartCustomizedRequest(
        @NotNull(message = "ID sản phẩm không được để trống")
        Long productId,

        @Min(value = 1, message = "Số lượng phải lớn hơn 0")
        @NotNull(message = "Số lượng không được để trống")
        Integer quantity,

        // --- BAG ---
        @NotNull(message = "Loại túi không được để trống")
        BagType bagType,
        String bagColorName, // Client gửi tên màu đã chọn trong JSON config

        // --- SHIRTS ---
        List<ShirtSelectionRequest> shirts,

        // --- LETTER ---
        String letterContent,

        // --- PHOTOBOOTH ---
        Long photoboothThemeId,

        // Danh sách các slots cho từng ảnh.
        // Ví dụ: [[1, 2, 3], [4, 5, 6]]
        // Nghĩa là: Ảnh đầu tiên (index 0) vào slot 1, 2, 3. Ảnh thứ hai (index 1) vào slot 4, 5, 6.
        List<List<Integer>> photoSlotIndices
) {}
