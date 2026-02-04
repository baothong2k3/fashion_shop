/*
 * @ (#) AttributeValueRepository.java    1.0    15/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.repositories;/*
 * @description:
 * @author: Bao Thong
 * @date: 15/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
    // Tìm value dựa trên tên giá trị và ID của thuộc tính cha
    Optional<AttributeValue> findByValueAndAttributeId(String value, Long attributeId);
}
