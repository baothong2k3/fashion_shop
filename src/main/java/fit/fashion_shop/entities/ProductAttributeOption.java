/*
 * @ (#) ProductAttributeOption.java    1.0    30/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.entities;/*
 * @description:
 * @author: Bao Thong
 * @date: 30/12/2025
 * @version: 1.0
 */

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_attribute_options")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id")
    private ProductAttribute attribute;

    private String value; // Tên hiển thị: "Gói vàng", "Khung A"

    private String imageUrl; // Ảnh chi tiết (ảnh lớn)
    private String thumbnailUrl; // Ảnh nhỏ (dùng cho việc chọn Áo trong combo)

    // Giá cộng thêm nếu chọn option này (VD: Gói xịn + 20k)
    private BigDecimal priceAdjustment;

    // Chứa thông tin bổ sung tùy biến.
    // Với Photobooth: lưu "3 photos". Với Dây buộc: lưu màu sắc.
    private String metaData;

    private boolean isDefault; // Nếu true, sẽ tự chọn nếu user không chọn
}
