/*
 * @ (#) ProductController.java    1.0    14/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.controllers;/*
 * @description:
 * @author: Bao Thong
 * @date: 14/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.ApiResponse;
import fit.fashion_shop.dtos.requests.CreateVariantRequest;
import fit.fashion_shop.dtos.requests.ProductRequest;
import fit.fashion_shop.dtos.responses.ProductResponse;
import fit.fashion_shop.dtos.responses.ProductWithVariantsResponse;
import fit.fashion_shop.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper; // Spring tự động cấu hình

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @RequestParam("productInfo") String productInfoJson,
            @RequestPart("thumbnailFile") MultipartFile thumbnailFile,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            HttpServletRequest httpReq) {

        try {
            // Chuyển chuỗi JSON sang DTO
            ProductRequest request = objectMapper.readValue(productInfoJson, ProductRequest.class);

            // Gọi service xử lý
            ProductResponse response = productService.createProduct(request, thumbnailFile, imageFiles);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(
                            HttpStatus.CREATED.value(),
                            "Thêm sản phẩm thành công",
                            response,
                            httpReq.getRequestURI()
                    ));
        } catch (Exception e) {
            throw new RuntimeException("Dữ liệu JSON không hợp lệ hoặc lỗi xử lý: " + e.getMessage());
        }
    }

    // API thêm variants cho sản phẩm
    @PostMapping(value = "/{id}/variants", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductWithVariantsResponse>> addVariants(
            @PathVariable Long id,
            @RequestParam("variants") String variantsJson, // Nhận JSON dưới dạng String
            @RequestPart(value = "files", required = false) List<MultipartFile> files, // Danh sách ảnh upload
            HttpServletRequest httpReq) {

        try {
            // 1. Convert String JSON sang List DTO
            List<CreateVariantRequest> requests = objectMapper.readValue(variantsJson, new TypeReference<>() {
            });

            // 2. Gọi Service xử lý (truyền thêm files)
            ProductWithVariantsResponse response = productService.createProductVariants(id, requests, files);

            return ResponseEntity.ok(ApiResponse.success(
                    HttpStatus.OK.value(),
                    "Thêm biến thể thành công",
                    response, // Trả về response chứa Product + Variants
                    httpReq.getRequestURI()
            ));
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý dữ liệu: " + e.getMessage());
        }
    }
}
