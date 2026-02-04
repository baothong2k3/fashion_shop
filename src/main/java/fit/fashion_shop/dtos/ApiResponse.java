/*
 * @ (#) ApiResponse.java    1.0    27/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos;/*
 * @description:
 * @author: Bao Thong
 * @date: 27/12/2025
 * @version: 1.0
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL) // Chỉ hiển thị các trường không null
public record ApiResponse<T>(
        int code,
        String message,
        T data, // Dùng để chứa dữ liệu trả về nếu có (ví dụ: Token, UserInfo)
        LocalDateTime timestamp,
        String path
) {
    // Helper để tạo nhanh response thành công không có data
    public static <T> ApiResponse<T> success(int code, String message, String path) {
        return new ApiResponse<>(code, message, null, LocalDateTime.now(), path);
    }

    // Helper để tạo nhanh response thành công có data
    public static <T> ApiResponse<T> success(int code, String message, T data, String path) {
        return new ApiResponse<>(code, message, data, LocalDateTime.now(), path);
    }

    // Helper để tạo nhanh response lỗi
    public static <T> ApiResponse<T> error(int code, String message, String path) {
        return new ApiResponse<>(code, message, null, LocalDateTime.now(), path);
    }
}
