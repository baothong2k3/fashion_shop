/*
 * @ (#) PhotoboothThemeResponse.java    1.0    24/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 24/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.PhotoboothTheme;

public record PhotoboothThemeResponse(
        Long id,
        String name,
        String previewImage,
        int slotsCount
) {
    public static PhotoboothThemeResponse fromEntity(PhotoboothTheme entity) {
        return new PhotoboothThemeResponse(
                entity.getId(),
                entity.getName(),
                entity.getPreviewImage(),
                entity.getSlotsCount()
        );
    }
}
