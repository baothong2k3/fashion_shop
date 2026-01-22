/*
 * @ (#) ProductCustomizationConfigRepository.java    1.0    22/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.repositories;/*
 * @description:
 * @author: Bao Thong
 * @date: 22/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.ProductCustomizationConfig;
import fit.fashion_shop.enums.StepType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCustomizationConfigRepository extends JpaRepository<ProductCustomizationConfig, Long> {
    Optional<ProductCustomizationConfig> findByProductIdAndStepType(Long productId, StepType stepType);
}
