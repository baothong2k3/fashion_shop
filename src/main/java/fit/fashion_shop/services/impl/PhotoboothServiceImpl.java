/*
 * @ (#) PhotoboothServiceImpl.java    1.0    24/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.services.impl;/*
 * @description:
 * @author: Bao Thong
 * @date: 24/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.requests.PhotoboothThemeRequest;
import fit.fashion_shop.dtos.requests.PhotoboothThemeUpdateRequest;
import fit.fashion_shop.dtos.responses.PhotoboothThemeResponse;
import fit.fashion_shop.entities.PhotoboothTheme;
import fit.fashion_shop.exceptions.DuplicateResourceException;
import fit.fashion_shop.exceptions.ResourceNotFoundException;
import fit.fashion_shop.repositories.PhotoboothThemeRepository;
import fit.fashion_shop.services.CloudinaryService;
import fit.fashion_shop.services.PhotoboothService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoboothServiceImpl implements PhotoboothService {

    private final PhotoboothThemeRepository photoboothThemeRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public PhotoboothThemeResponse createTheme(PhotoboothThemeRequest request) {
        // 1. Kiểm tra tên trùng lặp (Optional)
        if (photoboothThemeRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Tên chủ đề '" + request.name() + "' đã tồn tại");
        }

        // 2. Upload ảnh lên Cloudinary
        // Folder lưu trữ: photobooth/themes
        String imageUrl = cloudinaryService.uploadFile(request.imageFile(), "photobooth/themes");

        // 3. Tạo Entity
        PhotoboothTheme theme = PhotoboothTheme.builder()
                .name(request.name())
                .slotsCount(request.slotsCount())
                .previewImage(imageUrl)
                .build();

        // 4. Lưu và trả về response
        return PhotoboothThemeResponse.fromEntity(photoboothThemeRepository.save(theme));
    }

    @Override
    @Transactional
    public PhotoboothThemeResponse updateTheme(Long id, PhotoboothThemeUpdateRequest request) {
        // 1. Tìm theme cần sửa
        PhotoboothTheme theme = photoboothThemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chủ đề với ID: " + id));

        // 2. Cập nhật Tên (nếu có)
        if (request.name() != null && !request.name().isBlank()) {
            String newName = request.name().trim();
            // Nếu tên thay đổi, kiểm tra trùng lặp
            if (!newName.equals(theme.getName()) && photoboothThemeRepository.existsByName(newName)) {
                throw new DuplicateResourceException("Tên chủ đề '" + newName + "' đã tồn tại");
            }
            theme.setName(newName);
        }

        // 3. Cập nhật Số lượng slot (nếu có)
        if (request.slotsCount() != null) {
            theme.setSlotsCount(request.slotsCount());
        }

        // 4. Xử lý Ảnh (nếu có upload ảnh mới)
        if (request.imageFile() != null && !request.imageFile().isEmpty()) {
            // Bước 4.1: Xóa ảnh cũ trên Cloudinary (nếu tồn tại)
            if (theme.getPreviewImage() != null && !theme.getPreviewImage().isBlank()) {
                cloudinaryService.deleteFile(theme.getPreviewImage());
            }

            // Bước 4.2: Upload ảnh mới
            String newImageUrl = cloudinaryService.uploadFile(request.imageFile(), "photobooth/themes");
            theme.setPreviewImage(newImageUrl);
        }

        // 5. Lưu và trả về kết quả (Hibernate tự động update nhờ @Transactional)
        return PhotoboothThemeResponse.fromEntity(photoboothThemeRepository.save(theme));
    }

    @Override
    public List<PhotoboothThemeResponse> getAllThemes() {
        // Lấy tất cả từ DB và map sang DTO
        return photoboothThemeRepository.findAll()
                .stream()
                .map(PhotoboothThemeResponse::fromEntity)
                .toList();
    }
}
