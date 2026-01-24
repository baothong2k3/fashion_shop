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
import fit.fashion_shop.dtos.responses.PhotoboothThemeResponse;
import fit.fashion_shop.entities.PhotoboothTheme;
import fit.fashion_shop.exceptions.DuplicateResourceException;
import fit.fashion_shop.repositories.PhotoboothThemeRepository;
import fit.fashion_shop.services.CloudinaryService;
import fit.fashion_shop.services.PhotoboothService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
