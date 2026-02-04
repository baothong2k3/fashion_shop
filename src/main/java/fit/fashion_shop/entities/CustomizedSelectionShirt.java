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
@Table(name = "customized_selection_shirts")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selection_id", nullable = false)
    private CustomizedSelection selection;

    // --- Lưu trực tiếp giá trị thay vì liên kết AttributeValue ---
    private String size;

    private String colorName;
    private String colorCode;
    private String colorImage;
}