package com.ifuture.accountservice.controller;

import com.ifuture.accountservice.model.MetricsModel;
import com.ifuture.accountservice.service.AccountServiceStatistics;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/metrics")
public class AccountMetricsController {
    private final AccountServiceStatistics serviceStatistics;

    public AccountMetricsController(AccountServiceStatistics serviceStatistics) {
        this.serviceStatistics = serviceStatistics;
    }

    @GetMapping
    public ResponseEntity<MetricsModel> getAllRequests(@RequestParam String endpoint) throws IOException {
        return new ResponseEntity<>(serviceStatistics.getMetricsByEndpoint(endpoint), HttpStatus.OK);
    }
}
