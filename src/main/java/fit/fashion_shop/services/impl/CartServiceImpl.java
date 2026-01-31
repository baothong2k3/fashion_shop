/*
 * @ (#) CartServiceImpl.java    1.0    31/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.services.impl;/*
 * @description:
 * @author: Bao Thong
 * @date: 31/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.dtos.requests.AddToCartRequest;
import fit.fashion_shop.dtos.responses.CartResponse;
import fit.fashion_shop.entities.Cart;
import fit.fashion_shop.entities.CartItem;
import fit.fashion_shop.entities.Product;
import fit.fashion_shop.entities.ProductVariant;
import fit.fashion_shop.entities.User;
import fit.fashion_shop.exceptions.OperationNotPermittedException;
import fit.fashion_shop.exceptions.ResourceNotFoundException;
import fit.fashion_shop.repositories.CartItemRepository;
import fit.fashion_shop.repositories.CartRepository;
import fit.fashion_shop.repositories.ProductRepository;
import fit.fashion_shop.repositories.ProductVariantRepository;
import fit.fashion_shop.repositories.UserRepository;
import fit.fashion_shop.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CartResponse addToCart(Long userId, AddToCartRequest request) {
        // 1. Tìm hoặc tạo giỏ hàng cho User
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return cartRepository.save(Cart.builder().user(user).items(new ArrayList<>()).build());
        });

        // 2. Kiểm tra sản phẩm
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

        // Nếu sản phẩm là loại customizable nhưng request lại gọi vào API này (không có config)
        if (product.isCustomizable()) {
            throw new OperationNotPermittedException("Sản phẩm này yêu cầu tùy chỉnh thiết kế. Vui lòng sử dụng API khác.");
        }

        ProductVariant variant = null;
        int currentStock;

        // 3. Xử lý Biến thể (Variant) hoặc Sản phẩm thường
        if (request.variantId() != null) {
            // Case 1: Sản phẩm có biến thể
            variant = productVariantRepository.findById(request.variantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy biến thể sản phẩm"));

            // Validate: Variant phải thuộc về Product đó
            if (!variant.getProduct().getId().equals(product.getId())) {
                throw new OperationNotPermittedException("Biến thể không thuộc về sản phẩm này");
            }
            currentStock = variant.getStock();
        } else {
            // Case 2: Sản phẩm thường (không biến thể)
            // Kiểm tra xem sản phẩm có variants không, nếu có bắt buộc phải chọn variant
            boolean hasVariants = !productVariantRepository.findByProductId(product.getId()).isEmpty();
            if (hasVariants) {
                throw new OperationNotPermittedException("Sản phẩm này có nhiều phân loại, vui lòng chọn phân loại cụ thể.");
            }
            currentStock = product.getStock();
        }

        // 4. Kiểm tra tồn kho
        if (currentStock < request.quantity()) {
            throw new OperationNotPermittedException("Số lượng sản phẩm trong kho không đủ (Còn lại: " + currentStock + ")");
        }

        // 5. Thêm vào giỏ hoặc Cập nhật số lượng nếu đã tồn tại
        Optional<CartItem> existingItemOpt;
        if (variant != null) {
            existingItemOpt = cartItemRepository.findByCartIdAndProductIdAndVariantId(cart.getId(), product.getId(), variant.getId());
        } else {
            existingItemOpt = cartItemRepository.findByCartIdAndProductIdAndVariantIdIsNull(cart.getId(), product.getId());
        }

        if (existingItemOpt.isPresent()) {
            // Item đã có -> Cộng dồn số lượng
            CartItem existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + request.quantity();

            // Check tồn kho lại với số lượng mới
            if (currentStock < newQuantity) {
                throw new OperationNotPermittedException("Tổng số lượng trong giỏ vượt quá tồn kho (Kho: " + currentStock + ")");
            }
            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);
        } else {
            // Item chưa có -> Tạo mới
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .variant(variant) // Có thể null
                    .quantity(request.quantity())
                    .build();
            cartItemRepository.save(newItem);

            // Thêm vào list của cart để return response chính xác ngay lập tức (nếu không fetch lại DB)
            cart.getItems().add(newItem);
        }

        // 6. Trả về response
        // Refresh lại cart từ DB để đảm bảo data nhất quán nhất
        Cart updatedCart = cartRepository.findById(cart.getId()).orElse(cart);
        return CartResponse.fromEntity(updatedCart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getMyCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        return CartResponse.fromEntity(cart);
    }
}
