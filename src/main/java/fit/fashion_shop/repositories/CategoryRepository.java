/*
 * @ (#) CategoryRepository.java    1.0    13/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.repositories;/*
 * @description:
 * @author: Bao Thong
 * @date: 13/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Lấy danh sách danh mục gốc và đang hoạt động, sắp xếp theo thứ tự
    List<Category> findByParentIsNullAndIsActiveTrueOrderBySortOrderAsc();
}
