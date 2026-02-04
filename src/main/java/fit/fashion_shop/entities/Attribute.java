/*
 * @ (#) Attribute.java    1.0    09/01/2026
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
@Table(name = "attributes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., 'Color', 'Size'

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL)
    private List<AttributeValue> values;
}