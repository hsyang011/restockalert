package me.yangsongi.restockalert.controller;

import me.yangsongi.restockalert.dto.ApiResponse;
import me.yangsongi.restockalert.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductApiController {

    private final ProductService productService;

    @Autowired
    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    // 재입고 알림 전송 API
    @PostMapping("/products/{productId}/notifications/re-stock")
    public ResponseEntity<ApiResponse> sendRestockNotification(@PathVariable Long productId) {
        ApiResponse response = productService.sendRestockNotification(productId);

        return ResponseEntity.ok().body(response);
    }

}
