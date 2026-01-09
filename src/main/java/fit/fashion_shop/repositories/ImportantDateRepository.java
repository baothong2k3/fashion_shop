/*
 * @ (#) ImportantDateRepository.java    1.0    09/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.repositories;/*
 * @description:
 * @author: Bao Thong
 * @date: 09/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.ImportantDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImportantDateRepository extends JpaRepository<ImportantDate, Long> {
    Optional<ImportantDate> findByIdAndUserId(Long id, Long userId);
}
