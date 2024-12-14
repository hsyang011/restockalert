package me.yangsongi.restockalert.repository;

import me.yangsongi.restockalert.domain.ProductNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductNotificationHistoryRepository extends JpaRepository<ProductNotificationHistory, Long> {
}
