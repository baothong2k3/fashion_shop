/*
 * @ (#) Order.java    1.0    30/12/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package fit.fashion_shop.entities;/*
 * @description:
 * @author: Bao Thong
 * @date: 30/12/2025
 * @version: 1.0
 */

import fit.fashion_shop.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // Thông tin giao hàng (Snapshot lại từ Address tại thời điểm đặt)
    private String shippingAddress;
    private String shippingPhone;
    private String recipientName;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "order")
    private List<Payment> payments;
}
