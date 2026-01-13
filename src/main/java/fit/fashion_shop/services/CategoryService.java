/*
 * @ (#) CategoryService.java    1.0    13/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.services;/*
 * @description:
 * @author: Bao Thong
 * @date: 13/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.requests.CategoryRequest;
import fit.fashion_shop.dtos.requests.CategoryUpdateRequest;
import fit.fashion_shop.dtos.responses.CategoryResponse;
import fit.fashion_shop.dtos.responses.CategoryTreeResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Long id, CategoryUpdateRequest request);
    void deleteCategory(Long id);
    List<CategoryTreeResponse> getCategoryTree();
}
