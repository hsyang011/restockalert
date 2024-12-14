package me.yangsongi.restockalert.service;

import lombok.RequiredArgsConstructor;
import me.yangsongi.restockalert.domain.Product;
import me.yangsongi.restockalert.domain.ProductNotificationHistory;
import me.yangsongi.restockalert.domain.ProductUserNotification;
import me.yangsongi.restockalert.domain.ProductUserNotificationHistory;
import me.yangsongi.restockalert.dto.ApiResponse;
import me.yangsongi.restockalert.repository.ProductNotificationHistoryRepository;
import me.yangsongi.restockalert.repository.ProductRepository;
import me.yangsongi.restockalert.repository.ProductUserNotificationHistoryRepository;
import me.yangsongi.restockalert.repository.ProductUserNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductNotificationHistoryRepository notificationHistoryRepository;
    private final ProductUserNotificationRepository userNotificationRepository;
    private final ProductUserNotificationHistoryRepository userNotificationHistoryRepository;

    // 재입고 알림 전송 로직
    @Transactional
    public ApiResponse sendRestockNotification(Long productId) {
        // 1. 상품의 재입고 회차 증가
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        int restockCount = product.getRestockCount() + 1;
        product.setRestockCount(restockCount);
        productRepository.save(product);

        // 2. 알림을 설정한 유저들 조회 (알림 순서대로)
        List<ProductUserNotification> userNotifications = userNotificationRepository.findByProductAndIsActiveTrueOrderByCreatedAtAsc(product);

        // 3. 알림 상태 기록 생성
        ProductNotificationHistory notificationHistory = ProductNotificationHistory.builder()
                        .product(product)
                        .restockCount(restockCount)
                        .notificationStatus("IN_PROGRESS")
                        .lastUserId(null)
                        .build();
        notificationHistoryRepository.save(notificationHistory);

        // 4. 유저들에게 알림 전송
        for (int i = 0; i < userNotifications.size(); i++) {
            ProductUserNotification userNotification = userNotifications.get(i);

            // 알림 전송 (여기서는 실제로 알림을 보내는 로직 대신에 출력으로 대체할 수 있습니다)
            sendNotificationToUser(userNotification);

            // 5. 알림 전송 성공 시, 알림 히스토리 저장
            ProductUserNotificationHistory userNotificationHistory = ProductUserNotificationHistory.builder()
                            .product(product)
                            .userId(userNotification.getUserId())
                            .restockCount(restockCount)
                            .sentAt(LocalDateTime.now())
                            .build();
            userNotificationHistoryRepository.save(userNotificationHistory);

            // 6. 재고가 모두 소진된 경우, 알림 전송 중단
            if (product.getStockStatus().equals("OUT_OF_STOCK")) {
                notificationHistory.setNotificationStatus("CANCELED_BY_SOLD_OUT");
                notificationHistory.setLastUserId(userNotification.getUserId());
                notificationHistoryRepository.save(notificationHistory);
                break;  // 알림 전송 중단
            }

            // 알림 전송 상태 업데이트
            notificationHistory.setLastUserId(userNotification.getUserId());
            notificationHistoryRepository.save(notificationHistory);
        }

        // 7. 알림 전송 완료 처리
        notificationHistory.setNotificationStatus("COMPLETED");
        notificationHistoryRepository.save(notificationHistory);

        return new ApiResponse("success", "Restock notification sent successfully.", productId, restockCount);
    }

    // 유저에게 알림을 보내는 메서드 (실제 알림 전송 로직은 구현에 따라 다를 수 있음)
    public void sendNotificationToUser(ProductUserNotification userNotification) {
        // 실제 알림 전송 로직을 구현
        System.out.println("Sending restock notification to user " + userNotification.getUserId());
    }

}
