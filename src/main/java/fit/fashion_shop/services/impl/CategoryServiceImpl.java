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
import fit.fashion_shop.dtos.requests.CategoryUpdateRequest;
import fit.fashion_shop.dtos.responses.CategoryResponse;
import fit.fashion_shop.entities.Category;
import fit.fashion_shop.exceptions.OperationNotPermittedException;
import fit.fashion_shop.exceptions.ResourceNotFoundException;
import fit.fashion_shop.repositories.CategoryRepository;
import fit.fashion_shop.repositories.ProductRepository;
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
    private final ProductRepository productRepository;

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

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
        // 1. Tìm Category hiện tại
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id));

        // 2. Cập nhật danh mục cha (Parent)
        if (request.parentId() != null) {
            if (request.parentId() == 0) {
                // Quy ước: Nếu client gửi parentId = 0, hiểu là muốn chuyển thành danh mục gốc (null)
                category.setParent(null);
            } else {
                // Ngăn chặn việc gán cha là chính nó
                if (request.parentId().equals(id)) {
                    throw new OperationNotPermittedException("Danh mục cha không thể là chính nó");
                }

                // Tìm và gán danh mục cha mới
                Category parent = categoryRepository.findById(request.parentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục cha"));
                category.setParent(parent);
            }
        }

        // 3. Xử lý cập nhật ảnh (Xóa cũ trên Cloudinary, upload mới)
        if (request.imageFile() != null && !request.imageFile().isEmpty()) {
            // Xóa ảnh cũ
            cloudinaryService.deleteFile(category.getImage());
            // Upload và cập nhật URL mới vào database (thông qua Entity)
            String newImageUrl = cloudinaryService.uploadFile(request.imageFile(), "categories/images");
            category.setImage(newImageUrl);
        }

        // 4. Xử lý cập nhật icon tương tự ảnh
        if (request.iconFile() != null && !request.iconFile().isEmpty()) {
            cloudinaryService.deleteFile(category.getIcon());
            String newIconUrl = cloudinaryService.uploadFile(request.iconFile(), "categories/icons");
            category.setIcon(newIconUrl);
        }

        // 5. Cập nhật các trường thông tin khác
        if (request.name() != null && !request.name().isBlank()) {
            category.setName(request.name().trim());
        }
        if (request.slug() != null && !request.slug().isBlank()) {
            category.setSlug(request.slug().trim());
        }
        if (request.description() != null) {
            category.setDescription(request.description());
        }

        if (request.sortOrder() != null) {
            category.setSortOrder(request.sortOrder());
        }
        if (request.isActive() != null) {
            category.setActive(request.isActive());
        }

        // 6. Lưu và trả về kết quả
        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        // 1. Tìm danh mục cần xóa
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id));

        // 2. Bước kiểm tra mới: Nếu danh mục có sản phẩm thì không được xóa
        if (productRepository.existsByCategoryId(id)) {
            throw new OperationNotPermittedException("Không thể xóa danh mục vì đang chứa sản phẩm. Vui lòng xóa hoặc di chuyển sản phẩm trước.");
        }

        // 3. Xử lý danh mục con: Xóa liên kết với cha (chuyển con thành danh mục gốc)
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            category.getChildren().forEach(child -> child.setParent(null));
            categoryRepository.saveAll(category.getChildren());
        }

        // 4. Xóa ảnh và icon trên Cloudinary (sử dụng deleteFile của CloudinaryService)
        if (category.getImage() != null) {
            cloudinaryService.deleteFile(category.getImage());
        }
        if (category.getIcon() != null) {
            cloudinaryService.deleteFile(category.getIcon());
        }

        // 5. Xóa danh mục khỏi database
        categoryRepository.delete(category);
    }
}
