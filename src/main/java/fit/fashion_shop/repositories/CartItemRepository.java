/*
 * @ (#) CartItemRepository.java    1.0    31/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.repositories;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Tìm kiếm item đã tồn tại trong giỏ hàng (để cộng dồn số lượng)
    // Dành cho sản phẩm có biến thể (variantId khác null)
    Optional<CartItem> findByCartIdAndProductIdAndVariantId(Long cartId, Long productId, Long variantId);

    // Dành cho sản phẩm thường không có biến thể (variantId là null)
    Optional<CartItem> findByCartIdAndProductIdAndVariantIdIsNull(Long cartId, Long productId);
}
