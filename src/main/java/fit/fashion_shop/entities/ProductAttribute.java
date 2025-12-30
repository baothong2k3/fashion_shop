/*
 * @ (#) ProductAttribute.java    1.0    30/12/2025
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
@Table(name = "product_attributes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String name; // VD: "Gói quà", "Lá thư", "Photobooth"

    // Quy định số lượng được chọn. VD: Gói max=1. Nếu cần nhiều hơn set >1.
    private int maxSelection;

    private boolean isRequired; // Bắt buộc phải chọn hay không

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL)
    private List<ProductAttributeOption> options;
}
