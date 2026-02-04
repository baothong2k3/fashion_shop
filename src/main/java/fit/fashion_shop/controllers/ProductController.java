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
import fit.fashion_shop.dtos.requests.*;
import fit.fashion_shop.dtos.responses.ProductResponse;
import fit.fashion_shop.dtos.responses.ProductWithCustomizationResponse;
import fit.fashion_shop.dtos.responses.ProductWithVariantsResponse;
import fit.fashion_shop.enums.StepType;
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

    @PutMapping(value = "/variants/{variantId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductWithVariantsResponse>> updateVariant(
            @PathVariable Long variantId,
            @RequestParam("variantInfo") String variantInfoJson,
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
            // Nhận danh sách file ảnh cho thuộc tính (nếu có)
            @RequestPart(value = "attributeFiles", required = false) List<MultipartFile> attributeFiles,
            HttpServletRequest httpReq) {

        try {
            UpdateVariantRequest request = objectMapper.readValue(variantInfoJson, UpdateVariantRequest.class);

            ProductWithVariantsResponse response = productService.updateProductVariant(variantId, request, thumbnailFile, attributeFiles);

            return ResponseEntity.ok(ApiResponse.success(
                    HttpStatus.OK.value(),
                    "Cập nhật biến thể thành công",
                    response,
                    httpReq.getRequestURI()
            ));

        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý dữ liệu: " + e.getMessage());
        }
    }

    @DeleteMapping("/variants/{variantId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductWithVariantsResponse>> deleteVariant(
            @PathVariable Long variantId,
            HttpServletRequest httpReq) {

        ProductWithVariantsResponse response = productService.deleteProductVariant(variantId);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Xóa biến thể thành công",
                response,
                httpReq.getRequestURI()
        ));
    }

    @PutMapping(value = "/{id}/customization-config", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductWithCustomizationResponse>> updateCustomizationConfig(
            @PathVariable Long id,
            // Nhận JSON dưới dạng String để parse thủ công
            @RequestParam("configs") String configsJson,
            // Danh sách file ảnh (nếu có update ảnh màu áo)
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            HttpServletRequest httpReq) {

        try {
            // 1. Convert String JSON sang List DTO
            List<CustomizationConfigRequest> requests = objectMapper.readValue(configsJson, new TypeReference<>() {});

            // 2. Gọi Service xử lý
            ProductWithCustomizationResponse response = productService.saveCustomizationConfigs(id, requests, files);

            return ResponseEntity.ok(ApiResponse.success(
                    HttpStatus.OK.value(),
                    "Cập nhật cấu hình customize thành công",
                    response,
                    httpReq.getRequestURI()
            ));
        } catch (Exception e) {
            throw new RuntimeException("Dữ liệu JSON không hợp lệ hoặc lỗi xử lý: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/customization-config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCustomizationConfig(
            @PathVariable Long id,
            @RequestParam("stepType") StepType stepType,
            HttpServletRequest httpReq) {

        productService.deleteCustomizationConfig(id, stepType);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Xóa cấu hình customize thành công",
                httpReq.getRequestURI()
        ));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @RequestParam(value = "productInfo", required = false) String productInfoJson, // Cho phép null nếu chỉ muốn up ảnh
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            HttpServletRequest httpReq) {

        try {
            ProductUpdateRequest request;
            if (productInfoJson != null && !productInfoJson.isBlank()) {
                request = objectMapper.readValue(productInfoJson, ProductUpdateRequest.class);
            } else {
                // Nếu không gửi JSON, tạo object rỗng để code service không bị NullPointerException khi gọi getter
                request = new ProductUpdateRequest(null, null, null, null, null, null, null, null, null, null, null, null);
            }

            ProductResponse response = productService.updateProduct(id, request, thumbnailFile, imageFiles);

            return ResponseEntity.ok(ApiResponse.success(
                    HttpStatus.OK.value(),
                    "Cập nhật sản phẩm thành công",
                    response,
                    httpReq.getRequestURI()
            ));

        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý dữ liệu: " + e.getMessage());
        }
    }
}
