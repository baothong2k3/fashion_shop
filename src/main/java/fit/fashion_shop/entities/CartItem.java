/*
 * @ (#) CartItem.java    1.0    30/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.entities;/*
 * @description:
 * @author: Bao Thong
 * @date: 30/12/2025
 * @version: 1.0
 */

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "cart_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Nếu là sản phẩm thường -> lưu variants ID
    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    private int quantity;

    // Nếu là Combo Gift -> Lưu danh sách các option đã chọn
    @OneToMany(mappedBy = "cartItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemOption> selectedOptions;
}
