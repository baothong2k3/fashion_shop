/*
 * @ (#) CartItem.java    1.0    09/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.entities;/*
 * @description:
 * @author: Bao Thong
 * @date: 09/01/2026
 * @version: 1.0
 */

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // Nếu là SP thường
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    // Nếu là SP customize (Quan hệ 1-1 vì mỗi item thiết kế là duy nhất)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "selection_id")
    private CustomizedSelection selection;
}
