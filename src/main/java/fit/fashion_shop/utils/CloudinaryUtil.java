/*
 * @ (#) CloudinaryUtil.java    1.0    13/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.utils;/*
 * @description:
 * @author: Bao Thong
 * @date: 13/01/2026
 * @version: 1.0
 */

public class CloudinaryUtil {

    /**
     * Trích xuất publicId từ URL Cloudinary.
     * Hỗ trợ các định dạng: có/không có version (v123...), có/không có folder.
     */
    public static String extractPublicId(String url) {
        if (url == null || url.isBlank() || !url.contains("/upload/")) {
            return null;
        }

        try {
            // 1. Lấy phần sau "/upload/"
            String partAfterUpload = url.split("/upload/")[1];

            // 2. Bỏ phần version (ví dụ: "v1736785321/") nếu có
            // Version thường bắt đầu bằng 'v' và theo sau là các chữ số
            if (partAfterUpload.matches("^v[0-9]+/.*")) {
                partAfterUpload = partAfterUpload.substring(partAfterUpload.indexOf("/") + 1);
            }

            // 3. Loại bỏ phần mở rộng file (.jpg, .png, .webp...)
            int lastDotIndex = partAfterUpload.lastIndexOf(".");
            if (lastDotIndex != -1) {
                return partAfterUpload.substring(0, lastDotIndex);
            }

            return partAfterUpload;
        } catch (Exception e) {
            return null;
        }
    }
}
