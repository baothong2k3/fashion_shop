/*
 * @ (#) CloudinaryServiceImpl.java    1.0    13/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.services.impl;/*
 * @description:
 * @author: Bao Thong
 * @date: 13/01/2026
 * @version: 1.0
 */

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import fit.fashion_shop.services.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String folderName) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "folder", folderName
                    ));

            // Kiểm tra null để tránh NullPointerException khi lấy "secure_url"
            Object secureUrl = uploadResult.get("secure_url");
            return secureUrl != null ? secureUrl.toString() : null;

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi upload file lên Cloudinary: " + e.getMessage());
        }
    }
}
