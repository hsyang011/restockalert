package me.yangsongi.restockalert.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product_notification_history")
public class ProductNotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "restock_count", nullable = false)
    private int restockCount;  // 재입고 회차

    @Column(name = "notification_status", nullable = false)
    private String notificationStatus;  // 알림 발송 상태 (IN_PROGRESS, CANCELED_BY_SOLD_OUT 등)

    @Column(name = "last_user_id")
    private Long lastUserId;  // 마지막 발송 유저

}

