/*
 * @ (#) PhotoboothTheme.java    1.0    09/01/2026
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
@Table(name = "photobooth_themes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoboothTheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String previewImage;
    private int slotsCount;
}
