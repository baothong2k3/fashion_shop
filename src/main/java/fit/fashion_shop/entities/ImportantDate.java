/*
 * @ (#) ImportantDate.java    1.0    09/01/2026
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

import java.time.LocalDate;

@Entity
@Table(name = "important_dates")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportantDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    // Ví dụ: "Sinh nhật mẹ", "Kỷ niệm ngày cưới"

    @Column(nullable = false)
    private LocalDate date;

    @Builder.Default
    private Integer remindBeforeDays = 7;

    private boolean yearlyRepeat;
    // true cho các ngày lặp lại hàng năm như sinh nhật

    @Builder.Default
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
