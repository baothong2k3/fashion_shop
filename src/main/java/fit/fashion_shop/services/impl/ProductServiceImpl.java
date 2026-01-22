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

import fit.fashion_shop.dtos.requests.*;
import fit.fashion_shop.dtos.responses.ProductResponse;
import fit.fashion_shop.dtos.responses.ProductWithCustomizationResponse;
import fit.fashion_shop.dtos.responses.ProductWithVariantsResponse;
import fit.fashion_shop.entities.*;
import fit.fashion_shop.enums.StepType;
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
import tools.jackson.databind.ObjectMapper;

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
    private final ProductCustomizationConfigRepository customizationConfigRepository;
    private final ObjectMapper objectMapper;

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
    public ProductWithVariantsResponse createProductVariants(Long productId, List<CreateVariantRequest> requests, List<MultipartFile> files) {
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

        // 4. Lấy lại toàn bộ danh sách Variants (Cũ + Mới) của sản phẩm để trả về
        List<ProductVariant> allVariants = productVariantRepository.findByProductId(productId);

        return ProductWithVariantsResponse.fromEntity(product, allVariants);
    }

    @Override
    @Transactional
    public ProductWithVariantsResponse updateProductVariant(Long variantId, UpdateVariantRequest request, MultipartFile thumbnailFile, List<MultipartFile> attributeFiles) {
        // 1. Tìm biến thể cần update
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy biến thể với ID: " + variantId));

        // 2. Kiểm tra SKU
        if (request.sku() != null && !request.sku().equals(variant.getSku())) {
            if (productVariantRepository.existsBySku(request.sku())) {
                throw new DuplicateResourceException("SKU " + request.sku() + " đã tồn tại");
            }
            variant.setSku(request.sku());
        }

        // 3. Xử lý Ảnh Thumbnail của biến thể
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            // Xóa ảnh cũ của biến thể trên Cloudinary nếu tồn tại
            if (variant.getThumbnail() != null && !variant.getThumbnail().isBlank()) {
                cloudinaryService.deleteFile(variant.getThumbnail());
            }
            String newThumbnailUrl = cloudinaryService.uploadFile(thumbnailFile, "products/variants");
            variant.setThumbnail(newThumbnailUrl);
        }

        // 4. Cập nhật thông tin cơ bản
        if (request.priceOverride() != null) {
            variant.setPriceOverride(request.priceOverride());
        }
        if (request.stock() != null) {
            variant.setStock(request.stock());
        }

        // 5. Cập nhật thuộc tính (Attributes)
        if (request.attributes() != null) {
            // Xóa các liên kết thuộc tính cũ (orphanRemoval = true sẽ tự xóa DB)
            variant.getVariantAttributes().clear();

            // Helper: Tìm file trong list dựa trên tên file gửi lên từ JSON
            Function<String, MultipartFile> findFile = (filename) -> {
                if (attributeFiles == null || filename == null) return null;
                return attributeFiles.stream()
                        .filter(f -> filename.equals(f.getOriginalFilename()))
                        .findFirst()
                        .orElse(null);
            };

            for (VariantAttributeRequest attrReq : request.attributes()) {
                // Tìm hoặc tạo Attribute cha
                Attribute attribute = attributeRepository.findByName(attrReq.attributeName())
                        .orElseGet(() -> attributeRepository.save(Attribute.builder().name(attrReq.attributeName()).build()));

                // Tìm AttributeValue hiện có hoặc tạo mới builder (chưa save)
                AttributeValue attributeValue = attributeValueRepository.findByValueAndAttributeId(attrReq.value(), attribute.getId())
                        .orElse(AttributeValue.builder()
                                .attribute(attribute)
                                .value(attrReq.value())
                                .build());

                // --- LOGIC XỬ LÝ ẢNH ATTRIBUTE ---

                MultipartFile attrFile = findFile.apply(attrReq.imageUrl());

                // Nếu có file mới được upload
                if (attrFile != null) {
                    // 1. Kiểm tra và xóa ảnh cũ trên Cloudinary (nếu có)
                    String oldImageUrl = attributeValue.getImageUrl();
                    if (oldImageUrl != null && !oldImageUrl.isBlank()) {
                        cloudinaryService.deleteFile(oldImageUrl); // Gọi hàm deleteFile từ service
                    }

                    // 2. Upload ảnh mới và gán URL mới
                    String newAttrImageUrl = cloudinaryService.uploadFile(attrFile, "attributes");
                    attributeValue.setImageUrl(newAttrImageUrl);
                }

                // Cập nhật HexCode nếu có
                if (attrReq.hexCode() != null && !attrReq.hexCode().isBlank()) {
                    attributeValue.setHexCode(attrReq.hexCode());
                }

                // Lưu AttributeValue
                attributeValue = attributeValueRepository.save(attributeValue);

                // Liên kết với Variant
                ProductVariantAttribute variantAttribute = ProductVariantAttribute.builder()
                        .variant(variant)
                        .attributeValue(attributeValue)
                        .build();

                variant.getVariantAttributes().add(variantAttribute);
            }
        }

        // 6. Lưu biến thể
        productVariantRepository.save(variant);

        // 7. Trả về response
        Product product = variant.getProduct();
        List<ProductVariant> allVariants = productVariantRepository.findByProductId(product.getId());

        return ProductWithVariantsResponse.fromEntity(product, allVariants);
    }

    @Override
    @Transactional
    public ProductWithVariantsResponse deleteProductVariant(Long variantId) {
        // 1. Tìm biến thể cần xóa
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy biến thể với ID: " + variantId));

        // 2. Lưu lại Product gốc để query lại sau khi xóa
        Product product = variant.getProduct();
        Long productId = product.getId();

        // 3. Xóa ảnh Thumbnail trên Cloudinary (Nếu có)
        // Lưu ý: Không xóa ảnh của AttributeValue vì ảnh đó có thể dùng chung cho các biến thể khác hoặc sản phẩm khác
        if (variant.getThumbnail() != null && !variant.getThumbnail().isBlank()) {
            cloudinaryService.deleteFile(variant.getThumbnail());
        }

        // 4. Xóa biến thể trong Database
        // JPA sẽ tự động xóa các dòng trong bảng product_variant_attributes nhờ orphanRemoval=true trong Entity ProductVariant
        productVariantRepository.delete(variant);

        // 5. Flush để đảm bảo lệnh xóa được thực thi ngay lập tức trước khi query lại
        productVariantRepository.flush();

        // 6. Lấy lại danh sách biến thể còn lại của sản phẩm
        List<ProductVariant> remainingVariants = productVariantRepository.findByProductId(productId);

        // 7. Trả về response cấu trúc đầy đủ
        return ProductWithVariantsResponse.fromEntity(product, remainingVariants);
    }

    @Override
    @Transactional
    public ProductWithCustomizationResponse saveCustomizationConfigs(Long productId, List<CustomizationConfigRequest> requests) {
        // 1. Tìm sản phẩm
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + productId));

        // 2. Validate: Sản phẩm phải có flag customizable = true
        if (!product.isCustomizable()) {
            throw new OperationNotPermittedException("Sản phẩm này không được đánh dấu là Customizable. Vui lòng cập nhật sản phẩm trước.");
        }

        List<ProductCustomizationConfig> savedConfigs = new ArrayList<>();

        for (CustomizationConfigRequest req : requests) {
            // 3. Tìm config cũ (nếu có)
            ProductCustomizationConfig config = customizationConfigRepository
                    .findByProductIdAndStepType(productId, req.stepType())
                    .orElse(null);

            if (config == null) {
                // === TRƯỜNG HỢP TẠO MỚI ===
                // Nếu chưa có thì tạo mới, dùng giá trị từ request hoặc mặc định nếu null
                config = ProductCustomizationConfig.builder()
                        .product(product)
                        .stepType(req.stepType())
                        // Nếu req.enabled null -> mặc định là true
                        .isEnabled(req.enabled() != null ? req.enabled() : true)
                        // Nếu req.extraPrice null -> mặc định là 0.0
                        .extraPrice(req.extraPrice() != null ? req.extraPrice() : 0.0)
                        .build();

                // Xử lý JSON configData khi tạo mới
                if (req.configData() != null) {
                    try {
                        String jsonString = objectMapper.writeValueAsString(req.configData());
                        config.setConfigJson(jsonString);
                    } catch (Exception e) {
                        throw new RuntimeException("Lỗi khi convert configData sang JSON", e);
                    }
                }
            } else {
                // === TRƯỜNG HỢP CẬP NHẬT (Partial Update) ===

                // Chỉ cập nhật Enabled nếu request có gửi lên
                if (req.enabled() != null) {
                    config.setEnabled(req.enabled());
                }

                // Chỉ cập nhật ExtraPrice nếu request có gửi lên
                if (req.extraPrice() != null) {
                    config.setExtraPrice(req.extraPrice());
                }

                // Chỉ cập nhật ConfigJson nếu request có gửi lên data mới
                if (req.configData() != null) {
                    try {
                        String jsonString = objectMapper.writeValueAsString(req.configData());
                        config.setConfigJson(jsonString);
                    } catch (Exception e) {
                        throw new RuntimeException("Lỗi khi convert configData sang JSON", e);
                    }
                }
            }

            // 4. Lưu và thêm vào list kết quả
            savedConfigs.add(customizationConfigRepository.save(config));
        }

        // 5. Trả về Response bao gồm cả Product và List Config
        return ProductWithCustomizationResponse.fromEntity(product, savedConfigs, this.objectMapper);
    }

    @Override
    @Transactional
    public void deleteCustomizationConfig(Long productId, StepType stepType) {
        // 1. Kiểm tra sản phẩm tồn tại (để báo lỗi rõ ràng nếu sai ID)
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + productId);
        }

        // 2. Tìm config cần xóa
        ProductCustomizationConfig config = customizationConfigRepository
                .findByProductIdAndStepType(productId, stepType)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cấu hình cho bước " + stepType + " của sản phẩm này."));

        // 3. Thực hiện xóa
        customizationConfigRepository.delete(config);
    }
}