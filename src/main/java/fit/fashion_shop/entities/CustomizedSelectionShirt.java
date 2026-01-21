/*
 * @ (#) CustomizedSelectionShirt.java    1.0    21/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.entities;/*
 * @description:
 * @author: Bao Thong
 * @date: 21/01/2026
 * @version: 1.0
 */

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customized_selection_shirts", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"selection_id", "shirt_index"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomizedSelectionShirt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shirt_index", nullable = false)
    private int shirtIndex; // 1 hoặc 2

    // Quan hệ N-1 với Selection
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selection_id", nullable = false)
    private CustomizedSelection selection;

    // Size của áo (Link tới AttributeValue)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_attribute_value_id", nullable = false)
    private AttributeValue size;

    // Màu của áo (Link tới AttributeValue)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_attribute_value_id", nullable = false)
    private AttributeValue color;
}