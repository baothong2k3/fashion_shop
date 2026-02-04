/*
 * @ (#) CustomizedShirtResponse.java    1.0    31/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/01/2026
 * @version: 1.0
 */

public record CustomizedShirtResponse(
        int shirtIndex,
        String size,
        String colorName,
        String colorCode,
        String colorImage
) {}