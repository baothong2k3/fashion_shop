/*
 * @ (#) CustomizedSelectionPhoto.java    1.0    09/01/2026
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
@Table(name = "customized_selection_photos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomizedSelectionPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photoUrl;
    private int slotIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selection_id")
    private CustomizedSelection selection;
}
