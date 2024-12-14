package me.yangsongi.restockalert.repository;

import me.yangsongi.restockalert.domain.Product;
import me.yangsongi.restockalert.domain.ProductUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductUserNotificationRepository extends JpaRepository<ProductUserNotification, Long> {

    List<ProductUserNotification> findByProductAndIsActiveTrueOrderByCreatedAtAsc(Product product);

}
