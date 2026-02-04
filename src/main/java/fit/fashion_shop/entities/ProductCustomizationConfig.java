/*
 * @ (#) ProductCustomizationConfig.java    1.0    09/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.entities;/*
 * @description:
 * @author: Bao Thong
 * @date: 09/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.enums.StepType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_customization_config")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCustomizationConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StepType stepType; // e.g., BAG, SHIRT, LETTER, PHOTOBOOTH

    private boolean isEnabled;
    private Double extraPrice;

    @Column(columnDefinition = "JSON")
    private String configJson; // Giới hạn text, số lượng ảnh...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}