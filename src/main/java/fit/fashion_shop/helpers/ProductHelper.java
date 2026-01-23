/*
 * @ (#) ProductHelper.java    1.0    23/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.helpers;/*
 * @description:
 * @author: Bao Thong
 * @date: 23/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.responses.ProductDetailResponse;
import fit.fashion_shop.entities.Product;
import fit.fashion_shop.entities.ProductCustomizationConfig;
import fit.fashion_shop.entities.ProductVariant;
import fit.fashion_shop.repositories.ProductCustomizationConfigRepository;
import fit.fashion_shop.repositories.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductHelper {

    private final ProductVariantRepository productVariantRepository;
    private final ProductCustomizationConfigRepository customizationConfigRepository;
    private final ObjectMapper objectMapper;

    /**
     * Hàm phụ trách tổng hợp dữ liệu từ Product, Variants và Configs
     * để tạo ra ProductDetailResponse hoàn chỉnh.
     */
    public ProductDetailResponse buildProductDetailResponse(Product product) {
        // 1. Lấy danh sách biến thể (cho SP thường)
        List<ProductVariant> variants = productVariantRepository.findByProductId(product.getId());

        // 2. Lấy danh sách cấu hình customize (cho SP thiết kế)
        List<ProductCustomizationConfig> configs = customizationConfigRepository.findByProductId(product.getId());

        // 3. Map sang DTO
        return ProductDetailResponse.fromEntity(product, variants, configs, objectMapper);
    }
}