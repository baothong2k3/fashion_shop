/*
 * @ (#) ProductService.java    1.0    14/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.services;/*
 * @description:
 * @author: Bao Thong
 * @date: 14/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.requests.*;
import fit.fashion_shop.dtos.responses.ProductDetailResponse;
import fit.fashion_shop.dtos.responses.ProductResponse;
import fit.fashion_shop.dtos.responses.ProductWithCustomizationResponse;
import fit.fashion_shop.dtos.responses.ProductWithVariantsResponse;
import fit.fashion_shop.enums.StepType;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    // Create a new product with thumbnail and images
    ProductResponse createProduct(ProductRequest request, MultipartFile thumbnailFile, List<MultipartFile> imageFiles);

    // Create variants for an existing product
    ProductWithVariantsResponse createProductVariants(Long productId, List<CreateVariantRequest> requests, List<MultipartFile> files);

    // Update an existing product variant
    ProductWithVariantsResponse updateProductVariant(Long variantId, UpdateVariantRequest request, MultipartFile thumbnailFile, List<MultipartFile> attributeFiles);

    // Delete a product variant
    ProductWithVariantsResponse deleteProductVariant(Long variantId);

    // Save customization configurations for a product
    ProductWithCustomizationResponse saveCustomizationConfigs(Long productId, List<CustomizationConfigRequest> requests, List<MultipartFile> files);

    // Delete customization configuration for a specific step type
    void deleteCustomizationConfig(Long productId, StepType stepType);

    // Get public products with filtering and pagination
    Page<ProductResponse> getPublicProducts(
            Boolean newProduct,
            Boolean featured,
            Boolean bestSeller,
            Boolean customizable,
            String sort,
            int page,
            int size
    );

    // Get product details by ID
    ProductDetailResponse getProductDetail(Long id);

    // Get product details by slug
    ProductDetailResponse getProductDetailBySlug(String slug);

    // Update an existing product with thumbnail and images
    ProductResponse updateProduct(Long id, ProductUpdateRequest request, MultipartFile thumbnailFile, List<MultipartFile> imageFiles);
}
