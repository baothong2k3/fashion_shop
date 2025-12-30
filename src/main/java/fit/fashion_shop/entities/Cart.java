/*
 * @ (#) Cart.java    1.0    30/12/2025
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
@Table(name = "carts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items;
}
