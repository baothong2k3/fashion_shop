/*
 * @ (#) ImportantDateResponse.java    1.0    09/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.dtos.responses;/*
 * @description:
 * @author: Bao Thong
 * @date: 09/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.ImportantDate;

import java.time.LocalDate;

public record ImportantDateResponse(
        Long id,
        String title,
        LocalDate date,
        Integer remindBeforeDays,
        boolean yearlyRepeat,
        boolean active
) {
    // Helper method để chuyển đổi từ Entity sang Response DTO
    public static ImportantDateResponse fromEntity(ImportantDate entity) {
        return new ImportantDateResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getDate(),
                entity.getRemindBeforeDays(),
                entity.isYearlyRepeat(),
                entity.isActive()
        );
    }
}
