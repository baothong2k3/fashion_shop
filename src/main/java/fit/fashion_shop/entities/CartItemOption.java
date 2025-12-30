/*
 * @ (#) CartItemOption.java    1.0    30/12/2025
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

@Entity
@Table(name = "cart_item_options")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_item_id")
    private CartItem cartItem;

    @ManyToOne
    @JoinColumn(name = "product_attribute_option_id")
    private ProductAttributeOption productAttributeOption;
}