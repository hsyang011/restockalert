package me.yangsongi.restockalert.repository;

import me.yangsongi.restockalert.domain.Product;
import me.yangsongi.restockalert.domain.ProductNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductNotificationHistoryRepository extends JpaRepository<ProductNotificationHistory, Long> {

    // 최신 알림 기록 조회
    ProductNotificationHistory findTopByProductOrderByHistoryIdDesc(Product product);

}

