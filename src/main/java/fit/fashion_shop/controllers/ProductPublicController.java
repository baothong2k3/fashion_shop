/*
 * @ (#) ProductPublicController.java    1.0    23/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.controllers;/*
 * @description:
 * @author: Bao Thong
 * @date: 23/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.ApiResponse;
import fit.fashion_shop.dtos.responses.ProductDetailResponse;
import fit.fashion_shop.dtos.responses.ProductResponse;
import fit.fashion_shop.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductPublicController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProducts(
            @RequestParam(required = false) Boolean newProduct,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) Boolean bestSeller,
            @RequestParam(required = false) Boolean customizable,
            @RequestParam(required = false, defaultValue = "newest") String sort, // price_asc, price_desc, discount_desc
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            HttpServletRequest httpReq) {

        Page<ProductResponse> productPage = productService.getPublicProducts(
                newProduct, featured, bestSeller, customizable, sort, page, size
        );

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Lấy danh sách sản phẩm thành công",
                productPage,
                httpReq.getRequestURI()
        ));
    }

    // API lấy chi tiết theo ID: /api/products/1
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductById(
            @PathVariable Long id,
            HttpServletRequest httpReq) {

        ProductDetailResponse response = productService.getProductDetail(id);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Lấy chi tiết sản phẩm thành công",
                response,
                httpReq.getRequestURI()
        ));
    }

    // API lấy chi tiết theo Slug: /api/products/slug/ao-thun-mua-he
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductBySlug(
            @PathVariable String slug,
            HttpServletRequest httpReq) {

        ProductDetailResponse response = productService.getProductDetailBySlug(slug);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Lấy chi tiết sản phẩm thành công",
                response,
                httpReq.getRequestURI()
        ));
    }
}
