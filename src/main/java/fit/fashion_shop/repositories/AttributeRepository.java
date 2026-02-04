/*
 * @ (#) AttributeRepository.java    1.0    15/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.repositories;/*
 * @description:
 * @author: Bao Thong
 * @date: 15/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    Optional<Attribute> findByName(String name);
}
