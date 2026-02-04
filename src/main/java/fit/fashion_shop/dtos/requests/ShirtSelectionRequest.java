/*
 * @ (#) ShirtSelectionRequest.java    1.0    31/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/01/2026
 * @version: 1.0
 */

public record ShirtSelectionRequest(
        String size,
        String colorName // Client gửi tên màu để đối chiếu với Config
) {}
