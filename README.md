# Re-Stock Notification System

## 기술 스택

- **Backend Framework:** Spring Boot 3.4.0
- **Database:** MySQL 8.0 이상
- **ORM:** JPA (Hibernate)
- **디펜던시 관리:** Gradle
- **기타 라이브러리:** Lombok, Jakarta Persistence (JPA)

## 테이블 명세

### 1. **Product (상품)**

| 컬럼         | 설명               |
|--------------|--------------------|
| `product_id` | 상품 아이디 (Primary Key) |
| `restock_count` | 재입고 회차            |
| `stock_status` | 재고 상태 (IN_STOCK, OUT_OF_STOCK) |

### 2. **ProductNotificationHistory (상품별 재입고 알림 히스토리)**

| 컬럼              | 설명                      |
|-------------------|---------------------------|
| `history_id`      | 히스토리 고유 ID (Primary Key) |
| `product_id`      | 상품 아이디 (Foreign Key)   |
| `restock_count`   | 재입고 회차                 |
| `notification_status` | 알림 발송 상태 (IN_PROGRESS, CANCELED_BY_SOLD_OUT, CANCELED_BY_ERROR, COMPLETED) |
| `last_user_id`    | 마지막 발송 유저 ID         |

### 3. **ProductUserNotification (상품별 재입고 알림 설정 유저)**

| 컬럼              | 설명                    |
|-------------------|-------------------------|
| `notification_id` | 알림 설정 고유 ID (Primary Key) |
| `product_id`      | 상품 아이디 (Foreign Key) |
| `user_id`         | 유저 아이디               |
| `is_active`       | 알림 설정 활성화 여부     |
| `created_at`      | 생성 날짜                |
| `updated_at`      | 수정 날짜                |

### 4. **ProductUserNotificationHistory (상품 + 유저별 알림 히스토리)**

| 컬럼              | 설명                    |
|-------------------|-------------------------|
| `history_id`      | 히스토리 고유 ID (Primary Key) |
| `product_id`      | 상품 아이디 (Foreign Key) |
| `user_id`         | 유저 아이디               |
| `restock_count`   | 재입고 회차               |
| `sent_at`         | 발송 날짜                |

## 테이블 관계 설명

1. **Product (상품)**  
   - `Product` 테이블은 **상품** 정보를 저장하며, 상품 아이디(`product_id`)를 기준으로 다른 테이블과 연관됩니다.

2. **ProductNotificationHistory (상품별 재입고 알림 히스토리)**  
   - `ProductNotificationHistory` 테이블은 **상품별 재입고 알림 발송 히스토리**를 기록합니다.  
   - `product_id`는 `Product` 테이블의 외래키로, 하나의 상품은 여러 번의 알림 발송 기록을 가질 수 있습니다.
   - `last_user_id`는 알림을 마지막으로 보낸 유저의 아이디를 기록합니다.

3. **ProductUserNotification (상품별 재입고 알림 설정 유저)**  
   - `ProductUserNotification` 테이블은 **상품에 대해 재입고 알림을 설정한 유저들**을 기록합니다.  
   - 하나의 유저는 여러 상품에 대해 알림을 설정할 수 있습니다.
   - `product_id`는 `Product` 테이블의 외래키로 연결됩니다.

4. **ProductUserNotificationHistory (상품 + 유저별 알림 히스토리)**  
   - `ProductUserNotificationHistory` 테이블은 **각 유저가 받은 알림 히스토리**를 기록합니다.
   - `product_id`와 `user_id`를 외래키로 사용하며, 유저별로 알림 전송 이력을 저장합니다.
   - `restock_count`는 해당 회차의 재입고 알림이 발송된 회차를 기록합니다.

## 트러블슈팅

### 문제 1: "Product not found" 예외가 발생하는데 재입고 회차가 증가하는 현상

**문제**  
`productRepository.findById(productId)` 메서드에서 "Product not found" 예외가 발생하는 경우에도 재입고 회차(`restockCount`)가 증가하는 현상이 발생하였습니다.

**원인**  
`findById(productId)` 메서드가 상품을 찾지 못한 경우 예외를 던져야 하지만, 예외가 발생하기 전에 `restockCount`가 증가하는 코드가 먼저 실행되었습니다. 이로 인해 상품이 존재하지 않음에도 불구하고 재입고 회차가 잘못 증가하는 문제가 발생했습니다.

**해결 과정**
1. `productRepository.findById(productId)`에서 상품을 찾은 후에만 재입고 회차를 증가시키도록 수정했습니다.
2. `findById` 메서드에서 상품을 찾지 못하면 즉시 `IllegalArgumentException`을 던져, 이후 코드가 실행되지 않도록 했습니다.
3. 예외 발생 시, `restockCount`가 증가하지 않도록 보장했습니다.

**수정된 코드**

```java
@Transactional
public ApiResponse sendRestockNotification(Long productId) {
    // 1. 상품의 재입고 회차 증가
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

    // 상품을 찾았으므로, 재입고 회차 증가
    int restockCount = product.getRestockCount() + 1;
    product.setRestockCount(restockCount);
    productRepository.save(product);

    // ... 나머지 로직
}

---

이 README 파일은 시스템을 이해하는 데 필요한 주요 정보를 제공하며, 문제 발생 시 참고할 수 있는 트러블슈팅 가이드를 포함하고 있습니다. 추가적인 질문이나 수정이 필요하면 언제든지 말씀해주세요!
