package me.yangsongi.restockalert.repository;

import me.yangsongi.restockalert.domain.ProductUserNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductUserNotificationHistoryRepository extends JpaRepository<ProductUserNotificationHistory, Long> {
}
