/*
 * @ (#) OrderItem.java    1.0    30/12/2025
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
import java.util.List;

@Entity
@Table(name = "order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Snapshot giá tại thời điểm mua (vì giá product có thể đổi sau này)
    private BigDecimal price;
    private int quantity;

    // Lưu variant nếu là đồ thường
    private String variantName; // Lưu chuỗi "Size L - Red" để dễ hiển thị

    // Lưu các option đã chọn nếu là Combo
    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL)
    private List<OrderItemOption> selectedOptions;
}
