/*
 * @ (#) Product.java    1.0    09/01/2026
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
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price; // Giá cơ bản
    private Double salePrice;
    private Integer discount;
    private String thumbnail;
    private Integer stock;

    @Builder.Default
    private boolean newProduct = true;

    @Builder.Default
    private boolean featured = true;

    @Builder.Default
    private boolean bestSeller = true;

    @Builder.Default
    private boolean customizable = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // Cho sản phẩm thường
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductVariant> variants;

    // Cho sản phẩm customize
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductCustomizationConfig> customizationConfigs;
}
