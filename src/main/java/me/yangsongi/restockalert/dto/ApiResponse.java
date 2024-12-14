package me.yangsongi.restockalert.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse {

    private String status;
    private String message;
    private Long productId;
    private int restockCount;

}
