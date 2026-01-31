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

    // --- Lưu trực tiếp thông tin màu túi ---
    private String bagColorName;
    private String bagColorCode;
    private String bagColorImage;

    @OneToMany(mappedBy = "selection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomizedSelectionShirt> shirts;

    @Column(columnDefinition = "TEXT")
    private String letterContent;

    // Vẫn giữ liên kết Theme vì Theme là Entity quản lý bởi Admin
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photobooth_theme_id")
    private PhotoboothTheme photoboothTheme;

    // Tổng giá trị của riêng phần Customize (Extra fees)
    private Double totalCustomPrice;

    @OneToMany(mappedBy = "selection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomizedSelectionPhoto> photos;
}
