package me.yangsongi.restockalert.service;

import me.yangsongi.restockalert.domain.Product;
import me.yangsongi.restockalert.domain.ProductUserNotification;
import me.yangsongi.restockalert.repository.ProductRepository;
import me.yangsongi.restockalert.repository.ProductUserNotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductUserNotificationRepository userNotificationRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    public void testSendRestockNotifications() {
        // Arrange
        Product product = new Product();
        product.setProductId(1L);  // 상품 ID 설정
        product.setRestockCount(0); // 재입고 회차 0으로 설정
        product.setStockStatus("IN_STOCK"); // 재고 상태 IN_STOCK으로 설정

        // ProductRepository 모킹: findById 호출 시 product를 반환하도록 설정
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // ProductUserNotification 생성 (알림을 설정한 유저)
        ProductUserNotification userNotification = new ProductUserNotification();
        userNotification.setUserId(1L);
        userNotification.setIsActive(true);  // 알림 활성화

        // UserNotificationRepository 모킹: 유저 알림 조회 시 userNotification을 반환하도록 설정
        when(userNotificationRepository.findByProductAndIsActiveTrueOrderByCreatedAtAsc(product))
                .thenReturn(Arrays.asList(userNotification));

        // Act
        productService.sendRestockNotification(1L);

        // Assert
        // 상품 저장 확인
        verify(productRepository, times(1)).save(product);

        // 유저 알림 조회 확인
        verify(userNotificationRepository, times(1))
                .findByProductAndIsActiveTrueOrderByCreatedAtAsc(product);

        // 알림 전송 메서드가 호출된 횟수 확인 (sendNotificationToUser가 호출되어야 함)
        verify(productService, times(1)).sendNotificationToUser(userNotification);
    }

}
