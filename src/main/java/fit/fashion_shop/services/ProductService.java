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

import fit.fashion_shop.dtos.requests.CreateVariantRequest;
import fit.fashion_shop.dtos.requests.ProductRequest;
import fit.fashion_shop.dtos.requests.UpdateVariantRequest;
import fit.fashion_shop.dtos.responses.ProductResponse;
import fit.fashion_shop.dtos.responses.ProductWithVariantsResponse;
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
}
