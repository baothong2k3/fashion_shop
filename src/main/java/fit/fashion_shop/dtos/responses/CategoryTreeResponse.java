/*
 * @ (#) CategoryTreeResponse.java    1.0    14/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 14/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.Category;

import java.util.List;
import java.util.stream.Collectors;

public record CategoryTreeResponse(
        Long id,
        String name,
        String slug,
        String description,
        String image,
        String icon,
        int sortOrder,
        List<CategoryTreeResponse> children
) {
    public static CategoryTreeResponse fromEntity(Category entity) {
        return new CategoryTreeResponse(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getDescription(),
                entity.getImage(),
                entity.getIcon(),
                entity.getSortOrder(),
                entity.getChildren() != null ?
                        entity.getChildren().stream()
                                .filter(Category::isActive) // Chỉ lấy các con đang hoạt động
                                .map(CategoryTreeResponse::fromEntity)
                                .collect(Collectors.toList())
                        : List.of()
        );
    }
}
