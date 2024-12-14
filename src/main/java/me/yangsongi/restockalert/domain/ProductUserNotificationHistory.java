package me.yangsongi.restockalert.domain;

import jakarta.persistence.*;
import lombok.*;
import me.yangsongi.restockalert.domain.Product;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_user_notification_history")
public class ProductUserNotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;  // 고유한 알림 기록 ID

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "restock_count", nullable = false)
    private int restockCount;  // 재입고 회차

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;  // 발송 날짜

}
