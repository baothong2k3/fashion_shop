/*
 * @ (#) PhotoboothThemeRepository.java    1.0    24/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.repositories;/*
 * @description:
 * @author: Bao Thong
 * @date: 24/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.PhotoboothTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoboothThemeRepository extends JpaRepository<PhotoboothTheme, Long> {
    boolean existsByName(String name);
}
