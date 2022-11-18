package com.ifuture.accountservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricsModel {
    private double totalRequests;
    private double totalTime;
    private int requestsPerSecond;
    private String uri;
    private Method method;
}
