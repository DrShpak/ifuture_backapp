package com.ifuture.accountservice.service;

import com.ifuture.accountservice.model.MetricsModel;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

public interface AccountStatistics {

    @Scheduled(fixedRate = 10 * 60 * 1000, initialDelay = 60 * 1000)
    void updateMetrics() throws IOException;

    MetricsModel getMetricsByEndpoint(String endpoint) throws IOException;
}
