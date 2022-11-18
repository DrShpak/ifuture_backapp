package com.ifuture.accountservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ifuture.accountservice.model.Method;
import com.ifuture.accountservice.model.MetricsModel;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

@Service
public class AccountServiceStatistics implements AccountStatistics {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceStatistics.class);
    @Value("${url.to.metrics}")
    private String URL;
    private final RestTemplate template;
    private File metricsFile;
    @Value("${file}")
    private String pathToFile;

    @Value("#{${endpoints}}")
    private Map<String, Method> endpoints;

    public AccountServiceStatistics(RestTemplate template) {
        this.template = template;
    }

    @PostConstruct
    public void init() throws IOException {
        this.metricsFile = new File(pathToFile);
        metricsFile.createNewFile();
    }

    @Scheduled(fixedRate = 10 * 60 * 1000, initialDelay = 60 * 1000)
    public void updateMetrics() throws IOException {
        //clear old metrics
        FileChannel.open(Paths.get(metricsFile.toURI()), StandardOpenOption.WRITE).truncate(0).close();

        // fill new metrics
        for (Map.Entry<String, Method> endpoint : this.endpoints.entrySet()) {
            String response = template.getForObject(URL, String.class, endpoint);
            ObjectMapper objectMapper = new ObjectMapper();
            var metricsModel = parseAndGetModel(response, objectMapper, endpoint);
            writeMetricsInFile(metricsModel,  objectMapper);
        }
    }

    @SneakyThrows
    private MetricsModel parseAndGetModel(String response, ObjectMapper objectMapper, Map.Entry<String, Method> endpoint) {
        ObjectNode root = new ObjectMapper().readValue(response, ObjectNode.class);
        ObjectNode[] measurements = objectMapper.readValue(root.get("measurements").toString(), ObjectNode[].class);
        double totalRequest = 0.0;
        double totalTime = 0.0;
        for (ObjectNode node : measurements) {
            if (node.get("statistic").asText().equals("COUNT")) {
                totalRequest = node.get("value").asDouble();
            }
            if (node.get("statistic").asText().equals("TOTAL_TIME")) {
                totalTime = node.get("value").asDouble();
            }
        }
        return new MetricsModel(totalRequest, totalTime, (int) (totalRequest / totalTime), endpoint.getKey(), endpoint.getValue());
    }

    private void writeMetricsInFile(MetricsModel metricsModel, ObjectMapper objectMapper) throws IOException {
        Files.writeString(metricsFile.toPath(), objectMapper.writeValueAsString(metricsModel) + System.lineSeparator(), StandardOpenOption.APPEND);
    }

    public MetricsModel getMetricsByEndpoint(String endpoint) throws IOException {
        var listOfMetrics = Files.readAllLines(Paths.get(pathToFile));
        MetricsModel model = null;
        ObjectMapper mapper = new ObjectMapper();
        for (String metricAsJson : listOfMetrics) {
            model = mapper.readValue(metricAsJson, MetricsModel.class);
            if (model.getUri().equals(endpoint)) {
                break;
            }
        }

        if (model != null)
            return model;
        else
            throw new RuntimeException("Something went wrong!");
    }
}
