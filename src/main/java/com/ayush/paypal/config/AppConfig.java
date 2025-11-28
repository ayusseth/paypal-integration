package com.ayush.paypal.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
public class AppConfig {

//our project is synchronous so we are using RestClient

    @Bean
        //using custom RestClient with connection pooling and timeouts
    RestClient restClient(RestClient.Builder builder) {
        log.info("Creating RestClient Bean using Builder");
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100); // Set max total connections
        connectionManager.setDefaultMaxPerRoute(100); // Set max connections per route
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .evictIdleConnections(TimeValue.ofSeconds(30))
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectionRequestTimeout(10000); //10 seconds - time to get connection from pool
        requestFactory.setConnectTimeout(10000); //10 seconds - time to establish TCP connection
        requestFactory.setReadTimeout(15000); //15 seconds - time waiting for server response

        return builder
                .requestFactory(requestFactory)
                .build();
    }
}