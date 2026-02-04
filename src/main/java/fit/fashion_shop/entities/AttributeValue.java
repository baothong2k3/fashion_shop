/*
 * @ (#) AttributeValue.java    1.0    09/01/2026
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
@Table(name = "attribute_values")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;
    private String hexCode;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;

    @OneToMany(mappedBy = "attributeValue")
    private List<ProductVariantAttribute> variantAttributes;
}
