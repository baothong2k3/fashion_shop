/*
 * @ (#) CategoryResponse.java    1.0    13/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 13/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.Category;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description,
        String image,
        String icon,
        int sortOrder,
        boolean isActive,
        Long parentId
) {
    public static CategoryResponse fromEntity(Category entity) {
        return new CategoryResponse(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getDescription(),
                entity.getImage(),
                entity.getIcon(),
                entity.getSortOrder(),
                entity.isActive(),
                entity.getParent() != null ? entity.getParent().getId() : null
        );
    }
}
