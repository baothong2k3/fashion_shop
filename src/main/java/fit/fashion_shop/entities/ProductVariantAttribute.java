/*
 * @ (#) ProductVariantAttribute.java    1.0    09/01/2026
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
@Table(name = "variant_attribute_values")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductVariantAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_value_id")
    private AttributeValue attributeValue;
}
