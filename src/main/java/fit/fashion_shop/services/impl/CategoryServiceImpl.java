/*
 * @ (#) CategoryServiceImpl.java    1.0    13/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.services.impl;/*
 * @description:
 * @author: Bao Thong
 * @date: 13/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.requests.CategoryRequest;
import fit.fashion_shop.dtos.responses.CategoryResponse;
import fit.fashion_shop.entities.Category;
import fit.fashion_shop.exceptions.ResourceNotFoundException;
import fit.fashion_shop.repositories.CategoryRepository;
import fit.fashion_shop.services.CategoryService;
import fit.fashion_shop.services.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        // 1. Xử lý logic phân cấp (Parent-Child)
        Category parent = null;
        if (request.parentId() != null) {
            parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục cha"));
        }

        // 2. Upload ảnh qua service riêng
        String imageUrl = cloudinaryService.uploadFile(request.imageFile(), "categories/images");
        String iconUrl = cloudinaryService.uploadFile(request.iconFile(), "categories/icons");

        // 3. Lưu Category
        Category category = Category.builder()
                .name(request.name())
                .slug(request.slug())
                .description(request.description())
                .image(imageUrl)
                .icon(iconUrl)
                .sortOrder(request.sortOrder())
                .isActive(request.isActive())
                .parent(parent)
                .build();

        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }
}
