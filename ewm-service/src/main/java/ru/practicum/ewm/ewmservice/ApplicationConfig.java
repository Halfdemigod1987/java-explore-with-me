package ru.practicum.ewm.ewmservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.ewm.statsclient.StatsClient;

@Configuration
public class ApplicationConfig {
    @Value("${statsService.url}")
    private String statsServiceUrl;

    @Bean
    public StatsClient statsClient() {
        return new StatsClient(statsServiceUrl, new RestTemplateBuilder());
    }
}
