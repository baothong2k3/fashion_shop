/*
 * @ (#) ProductServiceImpl.java    1.0    14/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.services.impl;/*
 * @description:
 * @author: Bao Thong
 * @date: 14/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.requests.ProductRequest;
import fit.fashion_shop.dtos.responses.ProductResponse;
import fit.fashion_shop.entities.Category;
import fit.fashion_shop.entities.Product;
import fit.fashion_shop.exceptions.ResourceNotFoundException;
import fit.fashion_shop.repositories.CategoryRepository;
import fit.fashion_shop.repositories.ProductRepository;
import fit.fashion_shop.services.CloudinaryService;
import fit.fashion_shop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request, MultipartFile thumbnailFile) {
        // 1. Kiểm tra danh mục tồn tại
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục"));

        // 2. Upload thumbnail lên Cloudinary
        String thumbnailUrl = cloudinaryService.uploadFile(thumbnailFile, "products/thumbnails");

        // 3. Tạo Entity Product
        Product product = Product.builder()
                .name(request.name())
                .slug(request.slug())
                .description(request.description())
                .price(request.price())
                .salePrice(request.salePrice())
                .discount(request.discount())
                .stock(request.stock())
                .thumbnail(thumbnailUrl)
                .newProduct(request.newProduct() != null ? request.newProduct() : true)
                .featured(request.featured() != null ? request.featured() : false)
                .bestSeller(request.bestSeller() != null ? request.bestSeller() : false)
                .customizable(request.customizable() != null ? request.customizable() : false)
                .category(category)
                .build();

        // 4. Lưu và trả về DTO
        return ProductResponse.fromEntity(productRepository.save(product));
    }
}