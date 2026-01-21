/*
 * @ (#) CustomizedSelection.java    1.0    09/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.entities;/*
 * @description:
 * @author: Bao Thong
 * @date: 09/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.enums.BagType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "customized_selections")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomizedSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "bag_type", nullable = false)
    private BagType bagType;

    private Long bagColorId; // ID từ AttributeValue

    @OneToMany(mappedBy = "selection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomizedSelectionShirt> shirts;

    @Column(columnDefinition = "TEXT")
    private String letterContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photobooth_theme_id")
    private PhotoboothTheme photoboothTheme;

    private Double totalCustomPrice;

    @OneToMany(mappedBy = "selection", cascade = CascadeType.ALL)
    private List<CustomizedSelectionPhoto> photos;
}
