/*
 * @ (#) ProductService.java    1.0    14/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.services;/*
 * @description:
 * @author: Bao Thong
 * @date: 14/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.requests.ProductRequest;
import fit.fashion_shop.dtos.responses.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request, MultipartFile thumbnailFile);
}
