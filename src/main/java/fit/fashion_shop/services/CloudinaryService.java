/*
 * @ (#) CloudinaryService.java    1.0    13/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.services;/*
 * @description:
 * @author: Bao Thong
 * @date: 13/01/2026
 * @version: 1.0
 */

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadFile(MultipartFile file, String folderName);
}