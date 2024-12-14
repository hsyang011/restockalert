package me.yangsongi.restockalert.repository;

import me.yangsongi.restockalert.domain.ProductUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductUserNotificationRepository extends JpaRepository<ProductUserNotification, Long> {
}
