package me.yangsongi.restockalert.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product")
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "restock_count", nullable = false)
    private int restockCount;  // 재입고 회차

    @Column(name = "stock_status", nullable = false)
    private String stockStatus;  // 재고 상태 (IN_STOCK, OUT_OF_STOCK으로 저장)

}
