/*
 * @ (#) ProductRepository.java    1.0    14/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.repositories;/*
 * @description:
 * @author: Bao Thong
 * @date: 14/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // Kiểm tra xem có sản phẩm nào thuộc danh mục này không
    boolean existsByCategoryId(Long categoryId);
    // Kiểm tra slug đã tồn tại chưa
    boolean existsBySlug(String slug);
    // Tìm sản phẩm theo slug
    Optional<Product> findBySlug(String slug);
}