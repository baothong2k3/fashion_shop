/*
 * @ (#) CategoryController.java    1.0    13/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.controllers;/*
 * @description:
 * @author: Bao Thong
 * @date: 13/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.ApiResponse;
import fit.fashion_shop.dtos.requests.CategoryRequest;
import fit.fashion_shop.dtos.responses.CategoryResponse;
import fit.fashion_shop.services.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')") // Chỉ ADMIN mới có quyền truy cập
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @ModelAttribute CategoryRequest request,
            HttpServletRequest httpReq) {

        CategoryResponse response = categoryService.createCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "Tạo danh mục thành công",
                        response,
                        httpReq.getRequestURI()
                ));
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @ModelAttribute CategoryRequest request,
            HttpServletRequest httpReq) {

        CategoryResponse response = categoryService.updateCategory(id, request);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Cập nhật danh mục thành công",
                response,
                httpReq.getRequestURI()
        ));
    }
}