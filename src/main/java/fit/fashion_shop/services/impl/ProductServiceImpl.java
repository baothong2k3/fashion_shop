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

import fit.fashion_shop.dtos.requests.CreateVariantRequest;
import fit.fashion_shop.dtos.requests.ProductRequest;
import fit.fashion_shop.dtos.requests.VariantAttributeRequest;
import fit.fashion_shop.dtos.responses.ProductResponse;
import fit.fashion_shop.entities.*;
import fit.fashion_shop.exceptions.DuplicateResourceException;
import fit.fashion_shop.exceptions.OperationNotPermittedException;
import fit.fashion_shop.exceptions.ResourceNotFoundException;
import fit.fashion_shop.repositories.*;
import fit.fashion_shop.services.CloudinaryService;
import fit.fashion_shop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;
    private final ProductVariantRepository productVariantRepository;
    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request, MultipartFile thumbnailFile, List<MultipartFile> imageFiles) {
        // 1. Kiểm tra danh mục tồn tại
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục"));

        // 2. Upload thumbnail lên Cloudinary
        String thumbnailUrl = cloudinaryService.uploadFile(thumbnailFile, "products/thumbnails");

        // 3. Upload danh sách ảnh
        List<String> imageUrls = new ArrayList<>();
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                if (file != null && !file.isEmpty()) {
                    // Upload từng file và thêm URL vào list
                    String url = cloudinaryService.uploadFile(file, "products/images");
                    if (url != null) {
                        imageUrls.add(url);
                    }
                }
            }
        }

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
                .images(imageUrls)
                .newProduct(request.newProduct() != null ? request.newProduct() : true)
                .featured(request.featured() != null ? request.featured() : false)
                .bestSeller(request.bestSeller() != null ? request.bestSeller() : false)
                .customizable(request.customizable() != null ? request.customizable() : false)
                .category(category)
                .build();

        // 4. Lưu và trả về DTO
        return ProductResponse.fromEntity(productRepository.save(product));
    }

    @Override
    @Transactional
    public void createProductVariants(Long productId, List<CreateVariantRequest> requests, List<MultipartFile> files) {
        // 1. Kiểm tra sản phẩm gốc
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

        if (product.isCustomizable()) {
            throw new OperationNotPermittedException("Sản phẩm customizable không hỗ trợ tạo variant này.");
        }

        // 2. Helper function: Tìm file trong list dựa trên tên
        Function<String, MultipartFile> findFile = (filename) -> {
            if (files == null || filename == null) return null;
            return files.stream()
                    .filter(f -> filename.equals(f.getOriginalFilename()))
                    .findFirst()
                    .orElse(null);
        };

        // 3. Duyệt request
        for (CreateVariantRequest req : requests) {
            if (productVariantRepository.existsBySku(req.sku())) {
                throw new DuplicateResourceException("SKU " + req.sku() + " đã tồn tại");
            }

            // XỬ LÝ ẢNH THUMBNAIL CỦA VARIANT
            String thumbnailFinal = req.thumbnail(); // Mặc định là string gửi lên
            MultipartFile thumbFile = findFile.apply(req.thumbnail());
            if (thumbFile != null) {
                // Nếu tìm thấy file có tên trùng khớp, upload và lấy URL
                thumbnailFinal = cloudinaryService.uploadFile(thumbFile, "products/variants");
            }

            ProductVariant variant = ProductVariant.builder()
                    .product(product)
                    .sku(req.sku())
                    .priceOverride(req.priceOverride() != null ? req.priceOverride() : product.getPrice())
                    .stock(req.stock())
                    .thumbnail(thumbnailFinal) // Lưu URL thật
                    .variantAttributes(new ArrayList<>())
                    .build();

            // XỬ LÝ THUỘC TÍNH (MÀU SẮC CÓ THỂ CÓ ẢNH)
            if (req.attributes() != null) {
                for (VariantAttributeRequest attrReq : req.attributes()) {
                    Attribute attribute = attributeRepository.findByName(attrReq.attributeName())
                            .orElseGet(() -> attributeRepository.save(Attribute.builder().name(attrReq.attributeName()).build()));

                    // Xử lý ảnh của Attribute Value (ví dụ icon màu)
                    String attrImageUrl = attrReq.imageUrl();
                    MultipartFile attrFile = findFile.apply(attrReq.imageUrl());
                    if (attrFile != null) {
                        attrImageUrl = cloudinaryService.uploadFile(attrFile, "attributes");
                    }

                    // Tìm hoặc tạo AttributeValue (Cần final variable để dùng trong lambda)
                    String finalAttrImageUrl = attrImageUrl;
                    AttributeValue attributeValue = attributeValueRepository.findByValueAndAttributeId(attrReq.value(), attribute.getId())
                            .orElseGet(() -> attributeValueRepository.save(
                                    AttributeValue.builder()
                                            .attribute(attribute)
                                            .value(attrReq.value())
                                            .hexCode(attrReq.hexCode())
                                            .imageUrl(finalAttrImageUrl) // Lưu URL thật
                                            .build()
                            ));

                    ProductVariantAttribute variantAttribute = ProductVariantAttribute.builder()
                            .variant(variant)
                            .attributeValue(attributeValue)
                            .build();
                    variant.getVariantAttributes().add(variantAttribute);
                }
            }
            productVariantRepository.save(variant);
        }
    }
}