/*
 * @ (#) Order.java    1.0    09/01/2026
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package fit.fashion_shop.entities;/*
 * @description:
 * @author: Bao Thong
 * @date: 09/01/2026
 * @version: 1.0
 */

import fit.fashion_shop.enums.OrderStatus;
import fit.fashion_shop.enums.PaymentMethod;
import fit.fashion_shop.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderCode; // Mã đơn hàng (VD: SHOP-123456)
    private Double totalAmount;
    private Double shippingFee;
    private Double finalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // PENDING, CONFIRMED, SHIPPING, COMPLETED, CANCELLED

    // Thông tin giao hàng tại thời điểm đặt (tránh trường hợp user đổi địa chỉ sau này)
    private String recipientName;
    private String phoneNumber;
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // COD, VNPAY, MOMO...

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // PENDING, COMPLETED, FAILED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private LocalDateTime createdAt;
}
