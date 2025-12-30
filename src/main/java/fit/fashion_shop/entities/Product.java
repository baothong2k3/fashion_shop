/*
 * @ (#) Product.java    1.0    30/12/2025
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
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal basePrice; // Giá cơ bản

    private String thumbnail; // Ảnh đại diện chính

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Phân loại: false = Áo quần thường, true = Combo Gift có thể customize
    private boolean isCustomizable;

    // --- Dành cho sản phẩm thường (Áo/Quần) ---
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductVariant> variants;

    // --- Dành cho sản phẩm Combo (Customize) ---
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductAttribute> attributes;
}
