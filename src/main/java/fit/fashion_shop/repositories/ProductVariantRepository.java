/*
 * @ (#) ProductVariantRepository.java    1.0    15/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.repositories;/*
 * @description:
 * @author: Bao Thong
 * @date: 15/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    boolean existsBySku(String sku);
    // Lấy danh sách biến thể của 1 sản phẩm
    List<ProductVariant> findByProductId(Long productId);
}